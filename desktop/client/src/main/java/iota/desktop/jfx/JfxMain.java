package iota.desktop.jfx;

import iota.client.gui.presenter.IoTaPresenter;
import iota.client.gui.presenter.Presenter;
import iota.client.model.EspManager;
import iota.desktop.jfx.views.DeviceView;
import iota.desktop.jfx.views.DevicesSelector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
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
        GridPane gp = makeScene();
        //gp.prefWidthProperty().bind(stage.widthProperty());
        stage.setScene(new Scene(gp));
        stage.sizeToScene();
        stage.show();
    }

    private GridPane makeScene() {
        GridPane gp = new GridPane();
        gp.add(new DevicesSelector(presenter), 0, 0);
        DeviceView devView = new DeviceView(presenter);
        gp.add(devView, 1, 0);

        devView.prefWidthProperty().bind(gp.prefWidthProperty());

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHgrow(Priority.NEVER);
        gp.getColumnConstraints().add(column1);

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHgrow(Priority.ALWAYS);
        gp.getColumnConstraints().add(column2);
        return (gp);
    }

    @Override
    public void run() {
        activateJfx();
    }

    @Override
    public void stop(){
        System.exit(0);
    }


}
