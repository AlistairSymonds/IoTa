package iota.client.network;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class NetworkScanner {
    String baseIp = "192.168.0.";

    public List<ScanResult> scan(int port) {
        final ExecutorService es = Executors.newFixedThreadPool(255);
        final int timeout = 200;
        List<Future<ScanResult>> futures = new ArrayList<>();

        for (int i = 0; i <= 255; i++) {
            futures.add(portIsOpen(es, (baseIp + i), port, timeout));
        }
        es.shutdown();
        ArrayList<ScanResult> results = new ArrayList<>();
        boolean allDone = false;
        while (!allDone) {
            allDone = true;
            for (int i = 0; i < futures.size(); i++) {
                if (!futures.get(i).isDone()) {
                    allDone = false;
                }
            }
        }
        System.out.println("all scanning done");
        System.out.println((futures.size()));
        for (int i = 0; i < futures.size(); i++) {
            try {
                results.add(futures.get(i).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    private static Future<ScanResult> portIsOpen(final ExecutorService es, final String ip, final int port, final int timeout) {
        return es.submit(new Callable<ScanResult>() {
            @Override
            public ScanResult call() {
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(ip, port), timeout);
                    socket.close();
                    return new ScanResult(ip, port, true);
                } catch (Exception ex) {
                    return new ScanResult(ip, port, false);
                }
            }
        });
    }
}