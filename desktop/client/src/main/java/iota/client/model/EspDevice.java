package iota.client.model;

import iota.client.network.DataCapsule;
import iota.common.Constants;
import iota.common.IoTaUtil;
import iota.common.definitions.DefinitionStore;
import iota.common.definitions.HubInternal;
import iota.common.definitions.IFuncDef;
import iota.common.definitions.IFuncFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EspDevice extends Observable {

    private InetAddress address;
    private Socket socket;
    private List<IFuncDef> defs;
    private long deviceId;
    private boolean connected;
    private boolean authenticated = false;

    private DefinitionStore defStore;



    private SenderThread senderThread;
    private ReceiverThread receiverThread;



    public EspDevice(InetAddress address, DefinitionStore defStoreIn) {
        this.deviceId = 0;
        this.address = address;
        this.defStore = defStoreIn;
        defs = new ArrayList<>();
    }

    public synchronized InetAddress getInetAddress() {
        return this.address;
    }

    public synchronized boolean isConnected() {
        return false;
    }

    public synchronized List<IFuncDef> getFuncs() {
        return defs;
    }

    public synchronized boolean getAuthenticated() {
        return authenticated;
    }

    public void start() throws IOException {

        connected = true;
        socket = new Socket();
        System.out.println("Connecting to socket...");
        socket.connect(new InetSocketAddress(address, 2812));


        senderThread = new SenderThread();
        receiverThread = new ReceiverThread();

        receiverThread.start();
        senderThread.start();

        System.out.println("Sending magic byte");
        byte[] magic = new byte[1];
        magic[0] = 0x41;
        this.senderThread.sendMessage(magic);


        //manually crafting an empty capsule to send id
        DataCapsule capsule = new DataCapsule(new HubInternal(), new ArrayList<Byte>());

        //this.submitMessage(capsule);


        //senderThread.sendMessage(getFuncs);
    }


    public int submitMessage(DataCapsule capsule) {


        //convert Datacapsule to TCP packet

        byte[] msg = new byte[22 + capsule.getData().size()];
        short msgLen = (short) msg.length;
        msg[0] = (byte) (msgLen << 8);
        msg[1] = (byte) (msgLen);

        long thisId = capsule.getSourceId();
        for (int i = 0; i < 8; i++) {
            msg[10 - i] = (byte) (thisId << 8 * i);
        }
        long destId = this.deviceId;
        for (int i = 0; i < 8; i++) {
            msg[17 - i] = (byte) (thisId << 8 * i);
        }

        for (int i = 0; i < 1; i++) {
            msg[19 - i] = (byte) (capsule.getFuncId().getFuncId() << 8 * i);
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

    private int handleReturnedMessage(byte[] msg){

        //check if there are any data bytes after length and funcid
        if(msg.length<2){
            return -1;
        }
        //check if its an internal message (func id == 0x00FF)
        if(msg[1] == 0&& msg[2] == -1){
            for(int i = 4; i < msg.length; i++){
                IFuncDef f = IFuncFactory.getInstanceById(msg[i]);
                defs.add(f);
                System.out.println("Added func " + f.getFuncId() + " name " + f.getTableName());
            }
        }

        for (IFuncDef d : defs){
            if(d.getFuncId() == msg[2]){
                byte[] stateBuf = new byte[msg.length-3];
                System.arraycopy(msg,3,stateBuf,0,stateBuf.length);
                d.updateStateBuffer(stateBuf);
            }
        }


        setChanged();
        notifyObservers();
        return 0;
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
            this.notify();
        }

        @Override
        public synchronized void run() {
            System.out.println("Running " + this.toString());
            while (connected) {
                while (!queue.isEmpty()) {
                    try {
                        byte[] valsOut = queue.poll();

                        StringBuilder byteStr = new StringBuilder("");
                        for (int i = 0; i < valsOut.length; i++) {
                            byteStr.append("0x");
                            byteStr.append(String.format("%02x ", valsOut[i]));
                        }
                        System.out.println("Sent message: " + byteStr);

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
            while (connected) {
                int messageLength = 0;
                byte[] msg;

                try {
                    messageLength = (int) dataIn.readByte();
                    msg = new byte[messageLength];
                    msg[0] = (byte) messageLength;
                    dataIn.read(msg, 1, messageLength - 1);

                    StringBuilder byteStr = new StringBuilder("");
                    for (int i = 0; i < msg.length; i++) {
                        byteStr.append(String.format("%02x ", msg[i]));
                    }
                    System.out.println("Got message: " + byteStr);
                    handleReturnedMessage(msg);

                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }
    }
}
