package iota.client.gui.views;


import iota.client.gui.presenter.IoTaPresenter;
import iota.client.gui.views.functions.DebugView;
import iota.client.gui.views.functions.FunctionViewFactory;
import iota.client.model.EspDevice;
import iota.common.definitions.IFuncDef;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class DeviceView extends FlowPane implements UpdateAbleView {
    private IoTaPresenter presenter;
    private EspDevice device;
    private List<UpdateAbleView> funcViews;

    public DeviceView(IoTaPresenter presenterIn) {
        super();

        funcViews = new ArrayList<>();

        this.presenter = presenterIn;
        presenter.registerUpdateAbleView(this);
        this.setVgap(8);
        this.setHgap(4);
        updateDevice();

    }

    private void updateDevice() {

        if (presenter.getSelectedEspDevice() == null) {
            super.getChildren().clear();
            super.getChildren().add(new Text("No Device Selected"));

        } else if (presenter.getSelectedEspDevice() != device) {

            //new device, detroy all views and start over
            super.getChildren().clear();
            funcViews.clear();

            for (IFuncDef funcDef : presenter.getSelectedEspDevice().getFuncs()) {

                super.getChildren().add(FunctionViewFactory.getFunctionView(presenter.getSelectedEspDevice(), funcDef));
            }
            super.getChildren().add(new DebugView(presenter.getSelectedEspDevice()));

            for (Node n : super.getChildren()) {
                n.setStyle("-fx-border-color: #566954");
                if (n instanceof UpdateAbleView) {
                    funcViews.add((UpdateAbleView) n);
                    ((UpdateAbleView) n).updateView();
                }
            }




        } else {
            //same device, update views instead of destroying and recreating
            for (UpdateAbleView view : funcViews) {
                view.updateView();
            }
        }


        device = presenter.getSelectedEspDevice();

    }

    @Override
    public void updateView() {
        Platform.runLater(() -> updateDevice());
    }
    //for FUnction f : device.funcs
    //create layouts
}
