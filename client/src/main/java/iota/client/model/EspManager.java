package iota.client.model;

import iota.client.network.NetworkScanner;
import iota.client.network.ScanResult;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EspManager implements Runnable {
    private HashMap<InetAddress, EspDevice> devices;

    public EspManager() {
        devices = new HashMap<>();
    }

    public Map<InetAddress, EspDevice> getDevMap() {
        return devices;
    }

    @Override
    public void run() {
        updateMap();

    }

    private void updateMap() {
        NetworkScanner lanScan = new NetworkScanner();
        List<ScanResult> results = lanScan.scan(2812);
        for (ScanResult r : results) {
            if (r.isOpen()) {
                try (Socket conn = new Socket(r.getIP(), r.getPort())) {

                    if (devices.get(conn.getInetAddress()) == null) {
                        EspDevice espDevice = new EspDevice(conn);
                        System.out.println("Adding dev at" + espDevice.getInetAddress().toString());
                        devices.put(espDevice.getInetAddress(), espDevice);
                    } else {
                        System.out.println("device already exists in list");
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
