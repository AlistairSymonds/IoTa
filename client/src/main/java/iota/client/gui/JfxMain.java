package iota.client.gui;

import iota.client.model.EspManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class JfxMain extends Application implements Runnable {
    private static EspManager espManager;
    private Thread t;
    private String threadName = "jfx";
    ArrayList<String> testList = new ArrayList<>();

    private void activateJfx() {
        launch();
        testList.add("memes");
    }

    public static void setManager(EspManager espMng) {
        espManager = espMng;
    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        testList.add("spicy meme");
        DeviceList deviceListView = new DeviceList(espManager);
        GridPane gp = new GridPane();

        gp.add(deviceListView.getView(), 0, 0);

        Scene scene = new Scene(gp);

        stage.setTitle("IoTa Controller");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    @Override
    public void run() {
        activateJfx();
        testList.add("Blah");
    }


}
