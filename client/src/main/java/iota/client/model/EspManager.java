package iota.client.model;

import iota.client.network.NetworkScanner;
import iota.client.network.ScanResult;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

public class EspManager extends Observable implements Runnable {
    private Thread t;
    private String threadName;
    private ConcurrentHashMap<InetAddress, EspDevice> devices;
    private volatile boolean running = true;

    public EspManager() {
        devices = new ConcurrentHashMap<>();
        this.threadName = this.toString();
    }

    public Map<InetAddress, EspDevice> getDevMap() {
        return devices;
    }

    @Override
    public void run() {
        while (running) {
            updateMap();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    private void updateMap() {

        NetworkScanner lanScan = new NetworkScanner();
        List<ScanResult> results = lanScan.scan(2812);
        for (ScanResult r : results) {
            if (r.isOpen()) {
                if (!devices.containsKey(r.getHost())) {
                    EspDevice device = new EspDevice(r.getHost());
                    try {
                        device.start();
                    } catch (IOException e) {
                        System.out.println("exception, couldn't intialise");
                        e.printStackTrace();
                    }
                    devices.put(r.getHost(), device);
                }
            }
        }
        setChanged();
        notifyObservers();
    }
}
