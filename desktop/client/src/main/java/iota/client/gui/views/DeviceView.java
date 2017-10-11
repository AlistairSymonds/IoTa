package iota.client.gui.views;


import iota.client.gui.presenter.IoTaPresenter;
import iota.client.gui.views.functions.FunctionViewFactory;
import iota.common.definitions.IFuncDef;
import javafx.application.Platform;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class DeviceView extends FlowPane implements UpdateAbleView {
    IoTaPresenter presenter;

    public DeviceView(IoTaPresenter presenterIn) {
        super();
        this.presenter = presenterIn;
        presenter.registerUpdateAbleView(this);
        this.setVgap(8);
        this.setHgap(4);
        updateDevice();
    }

    private void updateDevice() {
        super.getChildren().clear();
        if (presenter.getSelectedEspDevice() == null) {
            super.getChildren().add(new Text("No Device Selected"));
        } else {
            for(IFuncDef func : presenter.getSelectedEspDevice().getFuncs()){
                Pane p = FunctionViewFactory.getFunctionView(presenter.getSelectedEspDevice(), func);

                p.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

                super.getChildren().add(p);
            }

        }
    }

    @Override
    public void updateView() {
        Platform.runLater(() -> updateDevice());
    }
    //for FUnction f : device.funcs
    //create layouts
}
