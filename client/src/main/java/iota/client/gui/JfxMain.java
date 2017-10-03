package iota.client.gui;

import iota.client.model.EspManager;
import iota.client.presenter.IoTaPresenter;
import iota.client.presenter.Presenter;
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
        DevicesSelector deviceListView = new DevicesSelector(presenter);
        GridPane gp = new GridPane();

        gp.add(deviceListView.getView(), 0, 0);
        DeviceView dv = new DeviceView(presenter.getSelectedEspDevice());
        gp.add(dv.getView(), 1, 0);

        Scene scene = new Scene(gp);

        stage.setTitle("IoTa Controller");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    @Override
    public void run() {
        activateJfx();
    }


}
