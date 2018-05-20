package iota.client.model;

import iota.client.network.ConcurrentConnectionStatus;
import iota.client.network.ConnectionStatus;
import iota.client.network.DataCapsule;
import iota.common.Constants;
import iota.common.IoTaUtil;
import iota.common.definitions.DefinitionStore;
import iota.common.functions.IFuncFactory;
import iota.common.functions.IFunction;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EspDevice extends Observable {

    private InetAddress address;
    private Socket socket;
    private List<IFunction> defs;
    private long deviceId;
    private volatile ConcurrentConnectionStatus status;


    private DefinitionStore defStore;



    private SenderThread senderThread;
    private ReceiverThread receiverThread;



    public EspDevice(InetAddress address, DefinitionStore defStoreIn) {
        this.deviceId = 0;
        this.address = address;
        this.defStore = defStoreIn;
        status = new ConcurrentConnectionStatus(ConnectionStatus.NOT_CONNECTED);

    }

    public synchronized InetAddress getInetAddress() {
        return this.address;
    }

    public synchronized List<IFunction> getFuncs() {
        //normally using handcafted function packets is bad
        //however some level of boot strapping is required to
        //get the first connection
        if (defs == null) {
            defs = new ArrayList<>();
            submitMessage(new DataCapsule(IoTaUtil.getHostId(),
                    deviceId,
                    Constants.FID_HUB,
                    IoTaUtil.byteArray2List(IoTaUtil.short2Bytes(Constants.FID_HUB))));
        }

        return defs;
    }

    public synchronized ConnectionStatus getStatus() {
        ConnectionStatus val = status.getStatus();
        return val;
    }

    public void start() throws IOException {


        socket = new Socket();
        System.out.println("Connecting to socket...");
        socket.connect(new InetSocketAddress(address, 2812));
        status.setStatus(ConnectionStatus.FIRST_CONNECT);

        senderThread = new SenderThread();
        receiverThread = new ReceiverThread();

        //this begins the authentication process before
        //settling into general handling of messages
        receiverThread.start();
        senderThread.start();

    }


    public int submitMessage(DataCapsule capsule) {


        //convert Datacapsule to TCP packet

        byte[] msg = new byte[22 + capsule.getData().size()];
        short msgLen = (short) msg.length;
        msg[0] = (byte) (msgLen << 8);
        msg[1] = (byte) (msgLen);

        byte[] sourceId = IoTaUtil.long2Bytes(capsule.getSourceId());
        System.arraycopy(sourceId, 0, msg, 2, 8);


        byte[] destArr;
        destArr = IoTaUtil.long2Bytes(this.deviceId);
        for (int i = 0; i < destArr.length; i++) {
            msg[10 + i] = destArr[i];
        }

        byte[] fidArr;
        fidArr = IoTaUtil.short2Bytes(capsule.getFuncId());
        for (int i = 0; i < fidArr.length; i++) {

            msg[18 + i] = fidArr[i];
        }

        byte[] dataLen;
        dataLen = IoTaUtil.short2Bytes((short) capsule.getData().size());
        for (int i = 0; i < dataLen.length; i++) {
            msg[20 + i] = dataLen[i];
        }

        List<Byte> data = capsule.getData();
        for (int i = 0; i < data.size(); i++) {
            msg[22 + i] = data.get(i);
        }

        senderThread.sendMessage(msg);
        setChanged();
        notifyObservers();
        return msg.length;
    }

    private int handleReturnedMessage(DataCapsule cap) {

        //check if there are any data bytes after length and funcid

        //check if its an internal message (func id == 0x00FF)
        if (cap.getFuncId() == Constants.FID_HUB) {

            for (int i = 0; i < cap.getData().size() / 2; i++) {

                IFunction f = IFuncFactory.getInstanceById((short) (cap.getData().get(2 * i + 1) << 8 | cap.getData().get(2 * i)));
                defs.add(f);
                System.out.println("Added func " + f.getFuncId() + " name " + f.getTableName());
            }
        }


        for (IFunction d : defs) {
            if (d.getFuncId() == cap.getFuncId()) {
                d.handleReceivedData(cap.getData());
            }
        }


        setChanged();
        notifyObservers();
        return 0;
    }

    public long getId() {
        return deviceId;
    }

    private DataCapsule createDataCapsuleFromArray(byte[] bytes) {
        long source = IoTaUtil.bytes2Long(Arrays.copyOfRange(bytes, 2, 10));
        long dest = IoTaUtil.bytes2Long(Arrays.copyOfRange(bytes, 10, 18));
        short fid = IoTaUtil.bytes2Short(Arrays.copyOfRange(bytes, 18, 20));
        short dataSize = IoTaUtil.bytes2Short(Arrays.copyOfRange(bytes, 20, 22));
        ArrayList<Byte> data = new ArrayList<>();
        for (int i = 22; i < dataSize + 22; i++) {
            data.add(bytes[i]);
        }
        DataCapsule cap = new DataCapsule(source, dest, fid, data);
        return cap;
    }

    private class SenderThread implements Runnable {
        private DataOutputStream dataOut;
        private ConcurrentLinkedQueue<byte[]> queue;
        private Thread t;

        public void start() throws IOException {
            queue = new ConcurrentLinkedQueue<>();
            dataOut = new DataOutputStream(socket.getOutputStream());
            t = new Thread(this);
            t.setName(getInetAddress() + "Sender");
            t.start();
        }

        synchronized void sendMessage(byte[] msg) {
            queue.add(msg);
            this.notifyAll();
        }

        @Override
        public synchronized void run() {
            System.out.println("Running " + this.toString());

            //while not in an error state handle various states and requirements
            while (status.getStatus() != ConnectionStatus.NOT_CONNECTED || status.getStatus() != ConnectionStatus.ERROR) {
                    switch (status.getStatus()) {
                        case FIRST_CONNECT:
                            status.setStatus(ConnectionStatus.SENDING_MAGIC_BYTE);
                            break;

                        case SENDING_MAGIC_BYTE:
                            try {
                                dataOut.write(Constants.MAGIC_BYTE);
                                status.setStatus(ConnectionStatus.WAITING_FOR_MAGIC_BYTE_PLUS_ONE);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;

                        case SENDING_EMPTY_CAPSULE:
                            DataCapsule initCap = new DataCapsule(IoTaUtil.getHostId(), 0, Constants.FID_HUB, new ArrayList<>());
                            submitMessage(initCap);
                            byte[] initBytes = queue.poll();


                            try {
                                dataOut.write(initBytes);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Sent empty capsule " + IoTaUtil.byteArr2HexStr(initBytes));
                            status.setStatus(ConnectionStatus.WAITING_FOR_EMPTY_CAPSULE);
                            break;

                        case CONNECTED:
                            while (!queue.isEmpty()) {
                                try {
                                    byte[] valsOut = queue.poll();

                                    System.out.println("Sent message: " + IoTaUtil.byteArr2HexStr(valsOut));
                                    dataOut.write(valsOut);
                                    setChanged();
                                    notifyObservers();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            try {
                                System.out.println(toString() + " has completed work, sleeping");
                                this.wait();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }



            }

            System.out.println("Sender for EspDevice @" + address.toString() + " shutting down");
        }
    }

    private class ReceiverThread implements Runnable {

        private DataInputStream dataIn;
        private Thread t;

        public void start() throws IOException {
            dataIn = new DataInputStream(socket.getInputStream());
            t = new Thread(this);
            t.start();
            t.setName(getInetAddress() + " Receiver");
            System.out.println("Starting \n" + this.toString());
        }

        @Override
        public synchronized void run() {

            System.out.println("Running " + this.toString());

            while (status.getStatus() != ConnectionStatus.NOT_CONNECTED || status.getStatus() != ConnectionStatus.ERROR) {
                synchronized (status) {
                    switch (status.getStatus()) {
                        case WAITING_FOR_MAGIC_BYTE_PLUS_ONE:
                            try {
                                byte val = dataIn.readByte();
                                if (val == Constants.MAGIC_BYTE + 1) {
                                    status.setStatus(ConnectionStatus.SENDING_EMPTY_CAPSULE);
                                } else {
                                    status.setStatus(ConnectionStatus.ERROR);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;

                        case WAITING_FOR_EMPTY_CAPSULE:
                            try {
                                byte[] emptyCapsule = new byte[23];
                                dataIn.read(emptyCapsule, 0, 22);
                                if (emptyCapsule[21] == 0 && emptyCapsule[22] == 0) {
                                    deviceId = IoTaUtil.bytes2Long(Arrays.copyOfRange(emptyCapsule, 2, 10));

                                    status.setStatus(ConnectionStatus.CONNECTED);
                                }
                                System.out.println("Empty capsule was: " + IoTaUtil.byteArr2HexStr(emptyCapsule));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;

                        case CONNECTED:


                            try {

                                byte[] sizeBytes = new byte[2];
                                dataIn.read(sizeBytes, 0, 2);
                                short msgLength = IoTaUtil.bytes2Short(sizeBytes);

                                byte[] msg = new byte[msgLength];
                                dataIn.read(msg, 2, msgLength - 2);
                                msg[0] = sizeBytes[0];
                                msg[1] = sizeBytes[1];

                                StringBuilder byteStr = new StringBuilder("");
                                for (int i = 0; i < msg.length; i++) {
                                    byteStr.append(String.format("%02x ", msg[i]));
                                }
                                System.out.println("Got message: " + byteStr);

                                handleReturnedMessage(createDataCapsuleFromArray(msg));

                            } catch (IOException e) {
                                status.setStatus(ConnectionStatus.ERROR);
                                e.printStackTrace();
                            }
                            break;

                    }

                }

            }
            System.out.println(this.toString() + " is finishing up");
        }
    }
}
