package iota.common.definitions;

import iota.common.Constants;
import iota.common.db.DbCol;

import java.util.List;

public class HubInternal implements IFuncDef {
    @Override
    public List<DbCol> getCols() {
        return null;
    }

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public short getFuncId() {
        return Constants.FID_HUB;
    }

    @Override
    public int submitMessage(byte[] message) {
        return 0;
    }

    @Override
    public int updateStateBuffer(byte[] recievedState) {
        return 0;
    }

    @Override
    public List<IStateItem> getStateItems() {
        return null;
    }


}
