package iota.client.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ScanResult {
    private byte[] IPV4;
    private int port;
    private boolean isOpen;
    private String host;
    private InetAddress inetAddress;

    public ScanResult(String host, int port, boolean isOpen) {
        this.host = host;
        this.port = port;
        this.isOpen = isOpen;

        try {
            this.inetAddress = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public InetAddress getHost() {
        return this.inetAddress;
    }

    public int getPort() {
        return this.port;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    @Override
    public String toString() {
        return "Port: " + port + " at IP: " + host + " is open? " + isOpen;
    }
}