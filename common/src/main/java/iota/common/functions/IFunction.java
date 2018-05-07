package iota.common.functions;


import iota.common.db.DbCol;
import iota.common.definitions.IStateItem;

import java.util.List;


public interface IFunction {
    List<DbCol> getCols();

    String getTableName();

    short getFuncId();


    int submitMessage(byte[] message);

    int handleReceivedData(List<Byte> receivedData);

    List<IStateItem> getStateItems();


}
