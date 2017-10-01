package iota.client.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class JfxMain extends Application implements Runnable {
    private void activateJfx() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Text text = new Text(10, 40, "Hello World!");
        text.setFont(new Font(40));
        Scene scene = new Scene(new Group(text));

        stage.setTitle("Welcome to JavaFX!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    @Override
    public void run() {
        activateJfx();
    }
}
