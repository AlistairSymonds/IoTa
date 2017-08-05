package iota.common.definitions;


import iota.common.definitions.db.DbCol;

import java.util.List;

public interface IFuncDef {
    List<DbCol> getCols();

    String getTableName();

    short getFuncId();
}
