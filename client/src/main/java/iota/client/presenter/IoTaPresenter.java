package iota.client.presenter;

import iota.client.model.EspDevice;

import java.util.List;

public interface IoTaPresenter {
    public EspDevice getSelectedEspDevice();

    public void setSelectedEspDevice(EspDevice devIn);

    public List<EspDevice> getDeviceList();

    public void resyncAll();


}
