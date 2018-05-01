package iota.common.functions;

import iota.common.Constants;
import iota.common.IoTaUtil;
import iota.common.db.DbCol;
import iota.common.definitions.IStateItem;

import java.util.ArrayList;
import java.util.List;

public class HubInternal implements IFunction {

    List<IStateItem> attachedFunctions;
    List<IStateItem> debugParameters;


    public HubInternal() {

    }

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
    public int handleReceivedData(List<Byte> recievedData) {
        return 0;
    }

    @Override
    public List<IStateItem> getStateItems() {
        return null;
    }


}
