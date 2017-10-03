package iota.client.gui.views;

import iota.client.gui.presenter.IoTaPresenter;
import iota.client.model.EspDevice;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;


public class DevicesSelector extends VBox implements UpdateAbleView {
    private ListView<EspDevice> lv;
    private Button updateButton;
    private IoTaPresenter presenter;

    public DevicesSelector(IoTaPresenter presenterIn) {
        super();
        this.presenter = presenterIn;
        presenter.registerUpdateAbleView(this);
        lv = new ListView<>();

        updateButton = new Button("Update List");
        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                lv.getItems().setAll(presenter.getDeviceList());
            }
        });
        lv.getItems().setAll(presenter.getDeviceList());

        lv.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    System.out.println("selected a dev");
                    presenter.setSelectedEspDevice(newValue);
                }
        );
        super.getChildren().add(updateButton);
        super.getChildren().add(lv);
    }

    @Override
    public void updateView() {
        lv.getItems().setAll(presenter.getDeviceList());
    }
}
