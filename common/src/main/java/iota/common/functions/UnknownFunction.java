package iota.common.functions;

import iota.client.model.EspDevice;
import iota.common.definitions.IStateItem;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class UnknownFunction implements IFunction {

    private short id;
    private EspDevice device;

    UnknownFunction(EspDevice device, int id) {
        this.id = (short) id;
        this.device = device;
    }

    public InetAddress getInetAdress() {
        return device.getInetAddress();
    }


    @Override
    public String getDisplayName() {
        return "Unkown";
    }

    @Override
    public short getFuncId() {
        return id;
    }

    @Override
    public int handleReceivedData(List<Byte> receivedData) {
        return 0;
    }

    @Override
    public List<IStateItem> getStateItems() {
        return new ArrayList<>();
    }
}
