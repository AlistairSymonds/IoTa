package iota.client.presenter;

import iota.client.model.EspDevice;
import iota.client.model.EspManager;

import java.util.ArrayList;
import java.util.List;

public class Presenter implements IoTaPresenter {
    private EspManager manager;
    private EspDevice selectedDevice;

    public Presenter(EspManager manager) {
        this.manager = manager;
    }

    @Override
    public EspDevice getSelectedEspDevice() {
        return selectedDevice;
    }

    @Override
    public void setSelectedEspDevice(EspDevice devIn) {
        this.selectedDevice = devIn;
    }

    @Override
    public List<EspDevice> getDeviceList() {
        List<EspDevice> l = new ArrayList<>();
        l.addAll(manager.getDevMap().values());
        return l;
    }

    @Override
    public void resyncAll() {
    }
}
