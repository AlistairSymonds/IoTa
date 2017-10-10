package iota.client.gui.views.functions;

import iota.client.model.EspDevice;
import javafx.scene.layout.Pane;

public class ErrorView implements IFunctionView {

    private final EspDevice device;

    public ErrorView(EspDevice deviceIn) {
        this.device = deviceIn;
    }

    @Override
    public Pane getView() {
        return null;
    }
}
