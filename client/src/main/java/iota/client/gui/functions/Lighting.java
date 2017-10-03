package iota.client.gui.functions;

import iota.client.model.EspDevice;
import javafx.scene.layout.Pane;

public class Lighting implements IFunctionView {
    private final EspDevice device;

    public Lighting(EspDevice deviceIn) {
        this.device = deviceIn;
    }

    @Override
    public Pane getView() {
        return null;
    }
}
