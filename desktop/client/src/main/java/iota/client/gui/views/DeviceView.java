package iota.client.gui.views;


import iota.client.gui.presenter.IoTaPresenter;
import iota.client.gui.views.functions.DebugView;
import iota.client.gui.views.functions.FunctionViewFactory;
import iota.client.gui.views.functions.IFunctionView;
import iota.client.model.EspDevice;
import iota.common.definitions.IFuncDef;
import javafx.application.Platform;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class DeviceView extends FlowPane implements UpdateAbleView {
    private IoTaPresenter presenter;
    private EspDevice device;
    private List<IFunctionView> funcViews;

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
            for(IFuncDef func : presenter.getSelectedEspDevice().getFuncs()){
                funcViews.add(FunctionViewFactory.getFunctionView(presenter.getSelectedEspDevice(), func));
            }

            for (IFunctionView view : funcViews) {
                super.getChildren().add(view.getView());
            }
            super.getChildren().add(new DebugView(presenter.getSelectedEspDevice()).getView());

        } else {
            //same device, update views instead of destroying and recreating
            for (IFunctionView view : funcViews) {
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
