package iota.client.model;

import iota.common.definitions.IFuncDef;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.channels.Selector;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EspDevice implements Runnable {

    private InetAddress address;
    private Socket socket;
    private List<IFuncDef> defs;
    private Thread t;
    private boolean connected;
    private ConcurrentLinkedQueue<byte[]> queue;

    private DataInputStream dataIn;
    private DataOutputStream dataOut;

    private Selector selector;

    public EspDevice(InetAddress address) {
        this.address = address;
        queue = new ConcurrentLinkedQueue<>();
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

    private void heartbeat() {
        byte[] heartBytes = {1, 60};
        sendMessage(heartBytes);
    }

    public void start() throws IOException {
        selector = Selector.open();
        connected = true;

        String tName = "EspDevice @" + this.address.toString();
        System.out.println("Starting " + tName);
        if (t == null) {
            t = new Thread(this, tName);
            t.start();
        }
    }

    public synchronized void run() {
        while (connected) {

            while (!queue.isEmpty()) {
                try {
                    writeOut(queue.poll());
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
        System.out.println("EspDevice @" + this.address.toString() + " shutting down");
    }

    public synchronized void sendMessage(byte[] messageData) {
        queue.add(messageData);
        this.notify();
    }

    private void writeOut(byte[] bytes) throws IOException {
        byte[] temp = new byte[bytes.length + 1];
        temp[0] = (byte) bytes.length;
        System.arraycopy(bytes, 0, temp, 1, bytes.length);
        dataOut.write(temp);
    }



}
