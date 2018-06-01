package iota.desktop.jfx.views.functions;

import iota.client.UpdateAbleView;
import iota.common.functions.UnknownFunction;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UnknownView extends VBox implements UpdateAbleView {
    private UnknownFunction funcInstance;

    protected UnknownView(UnknownFunction funcInstance) {
        super();
        this.funcInstance = funcInstance;

        super.getChildren().add(new Text("Unknown for device at" + funcInstance.getInetAdress()));


    }

    @Override
    public void updateView() {

    }
}
