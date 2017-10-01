package iota.client.gui;

import iota.client.model.EspDevice;
import iota.client.model.EspManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.util.ArrayList;

public class JfxMain extends Application implements Runnable {
    private EspManager espManager;
    private void activateJfx() {
        launch();
    }

    public void setManager(EspManager espMng) {
        this.espManager = espMng;
    }

    @Override
    public void start(Stage stage) throws Exception {
        GridPane gp = new GridPane();
        gp.add(createDeviceList(), 0, 0);
        gp.add(new Text("Test"), 0, 1);
        Scene scene = new Scene(gp);

        stage.setTitle("IoTa Controller");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    private ListView<EspDevice> createDeviceList() {
        ArrayList<EspDevice> devList = new ArrayList<EspDevice>();
        devList.addAll(espManager.getDevMap().values());
        ObservableList<EspDevice> observList = FXCollections.observableList(devList);
        return new ListView<>(observList);
    }

    @Override
    public void run() {
        activateJfx();
    }
}
