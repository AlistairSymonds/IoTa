package iota.client.model;

import java.net.InetAddress;
import java.net.Socket;

public class EspDevice implements Runnable {

    private Socket connection;


    public EspDevice(Socket connection) {
        this.connection = connection;
    }

    public synchronized InetAddress getInetAddress() {
        return connection.getInetAddress();
    }

    public void run() {
        // TODO handle data in and out

    }


}
