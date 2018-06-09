package iota.desktop.jfx.views.functions;

import iota.client.UpdateAbleView;
import iota.common.functions.IFunction;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UnknownView extends VBox implements UpdateAbleView {
    private IFunction funcInstance;

    protected UnknownView(IFunction funcInstance) {
        super();
        this.funcInstance = funcInstance;

        super.getChildren().add(new Text(new StringBuilder().append("Unknown func, ID:").append(funcInstance.getFuncId()).toString()));


    }

    @Override
    public void updateView() {

    }
}
