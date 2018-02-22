package iota.desktop.jfx.views.functions;

import iota.desktop.jfx.views.UpdateAbleView;
import iota.client.model.EspDevice;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ErrorView extends VBox implements UpdateAbleView {

    private final EspDevice device;

    protected ErrorView(EspDevice deviceIn) {
        super();
        this.device = deviceIn;


        super.getChildren().add(new Text("Unknown for device at" + device.getInetAddress()));


    }

    @Override
    public Node getView() {
        return this;
    }

    @Override
    public void updateView() {

    }
}
