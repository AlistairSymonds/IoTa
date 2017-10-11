package iota.client.gui.views.functions;

import iota.client.model.EspDevice;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ErrorView implements IFunctionView {

    private final EspDevice device;

    public ErrorView(EspDevice deviceIn) {
        this.device = deviceIn;
    }

    @Override
    public Pane getView() {

        Pane p = new Pane();

        p.getChildren().add(new Text("Unknown for device at" + device.getInetAddress()));

        return p;
    }
}
