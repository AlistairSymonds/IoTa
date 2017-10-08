package iota.client.model;

import iota.common.definitions.IFuncDef;

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
    private List<IFuncDef> defs;
    private Thread t;
    private boolean connected;


    private DataInputStream dataIn;
    private DataOutputStream dataOut;

    private SenderThread senderThread;
    private RecieverThread recieverThread;

    private long heartBeatStart;
    private long heartBeatFinish;


    public EspDevice(InetAddress address) {
        this.address = address;
        defs = new ArrayList<>();
    }

    public synchronized InetAddress getInetAddress() {
        return this.address;
    }

    public synchronized boolean isConnected() {
        return false;
    }

    public synchronized List<IFuncDef> getFuncs() {
        return null;
    }

    private void getFuncsFromDevice() {

    }

    public void heartbeat() {
        byte[] heartBytes = {1, 60};
        senderThread.sendMessage(heartBytes);
    }

    public void start() throws IOException {

        connected = true;
        socket = new Socket();
        socket.connect(new InetSocketAddress(address, 2812));
        dataIn = new DataInputStream(socket.getInputStream());
        dataOut = new DataOutputStream(socket.getOutputStream());


        senderThread = new SenderThread();
        recieverThread = new RecieverThread();

        senderThread.start();
        recieverThread.start();

    }

    public long getHeartBeatDelta() {
        return heartBeatFinish - heartBeatStart;
    }

    private void handleMessageRecieved(byte[] message) {
        switch (message[0]) {
            case 60:
                heartBeatFinish = System.currentTimeMillis();
                break;
            default:
                System.out.println("Unknown message from device");
        }

        setChanged();
        notifyObservers();
    }

    private void writeOut(byte[] bytes) throws IOException {

        switch (bytes[0]) {
            case 60:
                heartBeatStart = System.currentTimeMillis();
                break;
            default:
                System.out.println("Unkown message from device");
        }

        byte[] temp = new byte[bytes.length + 1];
        temp[0] = (byte) bytes.length;
        System.arraycopy(bytes, 0, temp, 1, bytes.length);
        dataOut.write(temp);
        setChanged();
        notifyObservers();
    }

    private class SenderThread implements Runnable {

        private ConcurrentLinkedQueue<byte[]> queue;
        Thread t;

        public void start() {
            queue = new ConcurrentLinkedQueue<>();
            t = new Thread(this);
            t.start();
        }

        public synchronized void sendMessage(byte[] msg) {
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
                        System.out.println("Sending: " + valsOut.toString());
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

    private class RecieverThread implements Runnable {
        private Thread t;

        public void start() {
            t = new Thread(this);
            t.start();
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
                    int bytesRead = dataIn.read(msg, 0, messageLength);
                    System.out.println("Got message: " + Arrays.toString(msg));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
