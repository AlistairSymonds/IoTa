package iota.client;

import iota.client.gui.JfxMain;
import iota.client.model.EspManager;

public class ClientMain {

    public static void main(String[] args) {


        EspManager devManager = new EspManager();
        devManager.start();

        JfxMain jfx = new JfxMain();
        JfxMain.setManager(devManager);

        jfx.start();

    }


}