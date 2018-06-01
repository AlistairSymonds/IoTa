package iota.common.functions;

import iota.client.model.EspDevice;
import iota.common.db.DbCol;
import iota.common.definitions.IStateItem;

import java.util.ArrayList;
import java.util.List;

public class Heartbeat implements IFunction {
    private EspDevice device;
    TimeDelta tDel;


    List<IStateItem> stateItems;

    protected Heartbeat(EspDevice device) {
        this.device = device;
        stateItems = new ArrayList<>();
        tDel = new TimeDelta();
        stateItems.add(tDel);
    }

    @Override
    public List<DbCol> getCols() {
        return null;
    }

    @Override
    public String getTableName() {
        return "heartbeat";
    }

    @Override
    public short getFuncId() {
        return 1;
    }

    @Override
    public int submitMessage(byte[] message) {

        tDel.timeOut = System.currentTimeMillis();
        return 0;
    }

    @Override
    public int handleReceivedData(List<Byte> receivedData) {
        tDel.timeIn = System.currentTimeMillis();


        return 1;

    }


    @Override
    public List<IStateItem> getStateItems() {
        return stateItems;
    }

    private class TimeDelta implements IStateItem {
        long timeOut = 0;
        long timeIn = 0;

        @Override
        public String getName() {
            return "TimeDelta";
        }

        @Override
        public String getVal() {
            return String.valueOf(timeIn - timeOut + "[ms]");
        }
    }
}
