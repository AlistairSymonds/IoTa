package iota.client.gui.views.functions;

import iota.client.model.EspDevice;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ErrorView implements IFunctionView {

    private final EspDevice device;

    protected ErrorView(EspDevice deviceIn) {
        this.device = deviceIn;
    }

    @Override
    public Pane getView() {

        VBox p = new VBox();

        p.getChildren().add(new Text("Unknown for device at" + device.getInetAddress()));

        return p;
    }

    @Override
    public void updateView() {

    }
}
