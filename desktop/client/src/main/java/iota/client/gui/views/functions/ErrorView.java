package iota.client.gui.views.functions;

import iota.client.gui.views.UpdateAbleView;
import iota.client.model.EspDevice;
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
    public void updateView() {

    }
}
