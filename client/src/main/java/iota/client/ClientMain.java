package iota.client;

import iota.client.gui.JfxMain;
import iota.client.network.NetworkScanner;
import iota.client.network.ScanResult;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClientMain {

    public static void main(String[] args) {

        NetworkScanner lanSearch = new NetworkScanner();

        List<ScanResult> results = null;
        try {
            results = lanSearch.scan(2812);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (results == null) {
            //big error is bad
            System.exit(1);
        }
        JfxMain jfx = new JfxMain();
        Thread jfxThread = new Thread(jfx);
        jfxThread.start();

        for (ScanResult res : results) {
            if (res.isOpen()) {
                System.out.println(res.toString());
            }
        }


        //scan
        //list
        //interact
    }


}