package iota.client.gui.presenter;

import iota.desktop.jfx.views.UpdateAbleView;
import iota.client.model.EspDevice;

import java.util.List;

public interface IoTaPresenter {
    public EspDevice getSelectedEspDevice();

    public void setSelectedEspDevice(EspDevice devIn);

    public List<EspDevice> getDeviceList();

    public void registerUpdateAbleView(UpdateAbleView view);

    public void removeUpdateAbleView(UpdateAbleView view);
}
