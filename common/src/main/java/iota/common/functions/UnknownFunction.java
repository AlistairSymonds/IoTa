package iota.common.functions;

import iota.common.db.DbCol;
import iota.common.definitions.IStateItem;

import java.util.ArrayList;
import java.util.List;

public class UnknownFunction implements IFunction {

    private short id;

    UnknownFunction(int id) {
        this.id = (short) id;
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
