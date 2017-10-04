package iota.client.gui.presenter;

import iota.client.gui.views.UpdateAbleView;
import iota.client.model.EspDevice;
import iota.client.model.EspManager;

import java.util.*;

public class Presenter implements IoTaPresenter, Observer {
    private EspManager manager;
    private EspDevice selectedDevice;
    private Set<UpdateAbleView> updateAbleViews;

    public Presenter(EspManager manager) {
        this.manager = manager;
        this.updateAbleViews = new HashSet<>();
        manager.addObserver(this);
    }

    @Override
    public void registerUpdateAbleView(UpdateAbleView newView) {
        updateAbleViews.add(newView);
    }

    @Override
    public void removeUpdateAbleView(UpdateAbleView view) {
        updateAbleViews.remove(view);
    }

    @Override
    public EspDevice getSelectedEspDevice() {
        return selectedDevice;
    }

    @Override
    public void setSelectedEspDevice(EspDevice devIn) {
        if (selectedDevice != null) {
            selectedDevice.deleteObserver(this);
        }

        this.selectedDevice = devIn;
        updateAttchedViews();
        selectedDevice.addObserver(this);
    }

    @Override
    public List<EspDevice> getDeviceList() {
        List<EspDevice> l = new ArrayList<>();
        l.addAll(manager.getDevMap().values());
        return l;
    }

    private void updateAttchedViews() {
        for (UpdateAbleView v : updateAbleViews) {
            v.updateView();
            System.out.println("Just updated view: " + v.toString());
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        updateAttchedViews();
    }
}
