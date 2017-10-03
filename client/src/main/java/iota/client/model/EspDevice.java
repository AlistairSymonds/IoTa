package iota.client.model;

import iota.common.definitions.IFuncDef;

import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class EspDevice implements Runnable {

    private Socket connection;
    private List<IFuncDef> defs;
    private boolean connected;

    public EspDevice(Socket connection) {
        this.connection = connection;
        this.connected = true;
    }

    public synchronized InetAddress getInetAddress() {
        return connection.getInetAddress();
    }

    public synchronized boolean isConnected() {
        return connection.isConnected();
    }

    public synchronized List<IFuncDef> getFuncs() {
        return null;
    }

    private void getFuncsFromDevice() {

    }

    public void run() {

        // TODO handle data in and out

    }


}
