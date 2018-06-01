package iota.common.functions;

import iota.client.model.EspDevice;
import iota.common.db.DbCol;
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
    public List<DbCol> getCols() {
        return new ArrayList<>();
    }

    @Override
    public String getTableName() {
        return "Unkown";
    }

    @Override
    public short getFuncId() {
        return id;
    }

    @Override
    public int submitMessage(byte[] message) {
        return 0;
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
