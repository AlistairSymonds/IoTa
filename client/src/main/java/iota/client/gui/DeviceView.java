package iota.client.gui;


import iota.client.model.EspDevice;
import javafx.scene.control.Control;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class DeviceView extends Control {
    EspDevice device;

    public DeviceView(EspDevice deviceIn) {
        this.device = deviceIn;
        //for FUnction f : device.funcs
        //create layouts
    }

    public Pane getView() {
        FlowPane fp = new FlowPane();
        if (device == null) {
            fp.getChildren().add(new Text("No device Selected"));
        } else {

        }
        return fp;
    }
}
