package iota.desktop.jfx.views;

import iota.client.UpdateAbleView;
import iota.client.gui.presenter.IoTaPresenter;
import iota.client.model.EspDevice;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;


public class DevicesSelector extends VBox implements UpdateAbleView {
    private ListView<EspDevice> lv;
    private IoTaPresenter presenter;

    public DevicesSelector(IoTaPresenter presenterIn) {
        super();
        this.presenter = presenterIn;
        presenter.registerUpdateAbleView(this);
        lv = new ListView<>();


        lv.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    System.out.println("selected a dev");
                    presenter.setSelectedEspDevice(newValue);
                }
        );
        super.getChildren().add(lv);
        updateView();
    }

    @Override
    public void updateView() {
        Platform.runLater(() -> internalUpdater());
    }

    private void internalUpdater() {
        for (EspDevice e : lv.getItems()) {
            if (!presenter.getDeviceList().contains(e)) {
                lv.getItems().remove(e);
            }
        }
        for (EspDevice e : presenter.getDeviceList()) {
            if (!lv.getItems().contains(e)) {
                lv.getItems().add(e);
            }
        }

    }
}
