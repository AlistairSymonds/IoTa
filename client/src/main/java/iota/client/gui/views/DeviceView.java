package iota.client.gui.views;


import iota.client.gui.presenter.IoTaPresenter;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

public class DeviceView extends FlowPane implements UpdateAbleView {
    IoTaPresenter presenter;

    public DeviceView(IoTaPresenter presenterIn) {
        super();
        this.presenter = presenterIn;
        presenter.registerUpdateAbleView(this);
    }

    private void updateDevice() {
        super.getChildren().clear();
        if (presenter.getSelectedEspDevice() == null) {
            super.getChildren().add(new Text("No Device Selected"));
        } else {
            super.getChildren().add(new Text(presenter.getSelectedEspDevice().toString()));
        }
    }

    @Override
    public void updateView() {
        updateDevice();
    }
    //for FUnction f : device.funcs
    //create layouts
}
