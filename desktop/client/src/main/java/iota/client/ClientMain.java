package iota.client;

import iota.client.gui.JfxMain;
import iota.client.model.EspManager;
import iota.common.definitions.DefinitionStore;

public class ClientMain {

    public static void main(String[] args) {




        DefinitionStore defStore = new DefinitionStore();

        EspManager devManager = new EspManager(defStore);
        devManager.start();

        JfxMain jfx = new JfxMain();
        JfxMain.setManager(devManager);

        jfx.start();

    }


}