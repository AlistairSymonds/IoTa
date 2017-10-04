package iota.client.gui.views;


import iota.client.gui.presenter.IoTaPresenter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

public class DeviceView extends FlowPane implements UpdateAbleView {
    IoTaPresenter presenter;

    public DeviceView(IoTaPresenter presenterIn) {
        super();
        this.presenter = presenterIn;
        presenter.registerUpdateAbleView(this);
        updateDevice();
    }

    private void updateDevice() {
        super.getChildren().clear();
        if (presenter.getSelectedEspDevice() == null) {
            super.getChildren().add(new Text("No Device Selected"));
        } else {
            super.getChildren().add(new Text(presenter.getSelectedEspDevice().toString()));
            Button hbeatBtn = new Button("Heartbeat");

            hbeatBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    presenter.getSelectedEspDevice().heartbeat();
                }
            });

            super.getChildren().add(hbeatBtn);

        }
    }

    @Override
    public void updateView() {
        Platform.runLater(() -> updateDevice());
    }
    //for FUnction f : device.funcs
    //create layouts
}
