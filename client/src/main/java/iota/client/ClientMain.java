package iota.client;

import iota.client.gui.JfxMain;
import iota.client.model.EspManager;

public class ClientMain {

    public static void main(String[] args) {


        EspManager devManager = new EspManager();
        Thread devManagerThread = new Thread(devManager);
        devManagerThread.start();

        JfxMain jfx = new JfxMain();
        jfx.setManager(devManager);

        Thread jfxThread = new Thread(jfx);
        jfxThread.start();


    }


}