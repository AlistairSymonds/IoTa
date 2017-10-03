package iota.client;

import iota.client.gui.JfxMain;
import iota.client.model.EspManager;
import iota.common.definitions.DefinitionStore;

public class ClientMain {

    public static void main(String[] args) {


        EspManager devManager = new EspManager();
        devManager.start();

        DefinitionStore defStore = new DefinitionStore();

        JfxMain jfx = new JfxMain();
        JfxMain.setManager(devManager);

        jfx.start();

    }


}