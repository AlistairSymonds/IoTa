package iota.client.gui;

import iota.client.model.EspDevice;
import iota.client.presenter.IoTaPresenter;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class DevicesSelector extends Control {
    private ListView<EspDevice> lv;
    private Button updateButton;
    private IoTaPresenter presenter;
    private ObservableList<EspDevice> obsList;

    public DevicesSelector(IoTaPresenter presenterIn) {
        this.presenter = presenterIn;

        lv = new ListView<>();

        updateButton = new Button("Update List");
        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("button");
                lv.getItems().setAll(presenter.getDeviceList());
            }
        });
        lv.getItems().setAll(presenter.getDeviceList());

        lv.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    presenter.setSelectedEspDevice(newValue);
                }
        );
    }


    public Pane getView() {
        VBox vBox = new VBox();
        vBox.getChildren().add(updateButton);
        vBox.getChildren().add(lv);
        return vBox;
    }
}
