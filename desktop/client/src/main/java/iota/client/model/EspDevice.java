package iota.client.model;

import iota.common.definitions.DefinitionStore;
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
    private boolean connected;

    private DefinitionStore defStore;



    private SenderThread senderThread;
    private ReceiverThread receiverThread;



    public EspDevice(InetAddress address, DefinitionStore defStoreIn) {
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


    public void start() throws IOException {

        connected = true;
        socket = new Socket();
        socket.connect(new InetSocketAddress(address, 2812));

        senderThread = new SenderThread();
        receiverThread = new ReceiverThread();

        receiverThread.start();
        senderThread.start();

        byte[] getFuncs = {0, (byte) 0xFF,(byte) 0xFF};
        senderThread.sendMessage(getFuncs);
    }


    public int submitMessage(byte[] msg){



        for(int i = 0; i < defs.size(); i++){
            if(defs.get(i).getFuncId() == msg[1]){
                defs.get(i).submitMessage(msg);
            }
        }

        senderThread.sendMessage(msg);
        setChanged();
        notifyObservers();
        return 0;
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

        public synchronized void sendMessage(byte[] msg) {
            queue.add(msg);
            this.notify();
        }

        private void writeOut(byte[] bytes) throws IOException {

            byte[] temp = new byte[bytes.length + 1];
            temp[0] = (byte) (bytes.length+1);


            System.arraycopy(bytes, 0, temp, 1, bytes.length);

            StringBuilder byteStr = new StringBuilder("");
            for (int i = 0; i < temp.length; i++) {
                byteStr.append(String.format("%02x ", temp[i]));
            }
            System.out.println("Sent message: " + byteStr);

            dataOut.write(temp);
            setChanged();
            notifyObservers();
        }


        @Override
        public synchronized void run() {
            System.out.println("Running " + this.toString());
            while (connected) {
                while (!queue.isEmpty()) {
                    try {
                        byte[] valsOut = queue.poll();
                        writeOut(valsOut);
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
                    e.printStackTrace();
                }
            }
        }
    }
}
