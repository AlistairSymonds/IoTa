package iota.client.gui;

import iota.client.model.EspDevice;
import iota.client.model.EspManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;


public class DeviceList extends Control {
    private ListView<EspDevice> lv;
    private Button updateButton;
    private EspManager data;


    public DeviceList(EspManager manager) {
        data = manager;

        lv = new ListView<>();

        updateButton = new Button("Update List");
        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                data.updateMap();
                resyncView();
            }
        });
        resyncView();

    }

    public void resyncView() {
        System.out.println("Syncing dev list view");
        lv.getItems().clear();
        lv.getItems().addAll(data.getDevMap().values());
    }

    public VBox getView() {
        VBox vBox = new VBox();
        vBox.getChildren().add(updateButton);
        vBox.getChildren().add(lv);
        return vBox;
    }
}
