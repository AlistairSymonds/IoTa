package iota.desktop.jfx.views.state;

import iota.common.definitions.IStateItem;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DefaultStateDisp extends VBox {

    private IStateItem state;
    private Text disp;

    public DefaultStateDisp(IStateItem stateItem) {
        super();
        state = stateItem;
        disp = new Text();
        super.getChildren().add(disp);
    }


    public void updateView() {
        disp.setText("State of " + state.getName() + " is " + state.getVal());
    }
}
