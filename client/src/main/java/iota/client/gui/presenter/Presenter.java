package iota.client.gui.presenter;

import iota.client.gui.views.UpdateAbleView;
import iota.client.model.EspDevice;
import iota.client.model.EspManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Presenter implements IoTaPresenter {
    private EspManager manager;
    private EspDevice selectedDevice;
    private Set<UpdateAbleView> updateAbleViews;

    public Presenter(EspManager manager) {
        this.manager = manager;
        this.updateAbleViews = new HashSet<>();
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
        this.selectedDevice = devIn;
        updateAttchedViews();
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


}
