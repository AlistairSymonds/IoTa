package iota.client.model;

import iota.client.network.NetworkScanner;
import iota.client.network.ScanResult;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EspManager implements Runnable {
    private Thread t;
    private String threadName;
    private ConcurrentHashMap<InetAddress, EspDevice> devices;

    public EspManager() {
        devices = new ConcurrentHashMap<>();
        this.threadName = this.toString();
    }

    public Map<InetAddress, EspDevice> getDevMap() {
        return devices;
    }

    @Override
    public void run() {
        updateMap();

    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    public void updateMap() {
        NetworkScanner lanScan = new NetworkScanner();
        List<ScanResult> results = lanScan.scan(2812);

        for (ScanResult r : results) {
            if (r.isOpen()) {
                try (Socket conn = new Socket(r.getHost(), r.getPort())) {
                    if (devices.get(conn.getInetAddress()) == null) {
                        EspDevice espDevice = new EspDevice(conn);
                        System.out.println("Adding dev at" + espDevice.getInetAddress().toString());

                        devices.put(espDevice.getInetAddress(), espDevice);

                    } else {
                        System.out.println("Device at " + r.getHost() + " already exists");
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                if (devices.containsKey(r.getHost())) {
                    devices.remove(r.getHost());
                    System.out.println("Device at " + r.getHost() + " not found, removing");
                }
            }
        }

    }
}
