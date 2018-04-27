package iota.common.definitions;


import iota.common.db.DbCol;

import java.util.List;

public interface IFuncDef {
    List<DbCol> getCols();

    String getTableName();

    short getFuncId();

    int submitMessage(byte[] message);

    int updateStateBuffer(byte[] recievedState);

    List<IStateItem> getStateItems();


}
