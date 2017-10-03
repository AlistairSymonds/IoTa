package iota.client.gui;

import iota.client.gui.presenter.IoTaPresenter;
import iota.client.gui.presenter.Presenter;
import iota.client.gui.views.DeviceView;
import iota.client.gui.views.DevicesSelector;
import iota.client.model.EspManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class JfxMain extends Application implements Runnable {
    private static EspManager espManager;
    private static IoTaPresenter presenter;
    private Thread t;
    private String threadName = "jfx";

    private void activateJfx() {
        launch();
    }

    public static void setManager(EspManager espMng) {
        espManager = espMng;
        presenter = new Presenter(espMng);
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

        stage.setTitle("IoTa Controller");
        stage.setScene(makeScene());
        stage.sizeToScene();
        stage.show();
    }

    private Scene makeScene() {
        GridPane gp = new GridPane();
        gp.add(new DevicesSelector(presenter), 0, 0);
        gp.add(new DeviceView(presenter), 1, 0);
        return new Scene(gp);
    }

    @Override
    public void run() {
        activateJfx();
    }


}
