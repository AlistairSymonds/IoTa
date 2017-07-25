package iota.common.definitions;


import iota.common.definitions.db.DbCol;
import iota.common.definitions.db.IDbQueryGenerator;

import java.util.List;

public interface IFuncDef {
    List<DbCol> getCols();

    String getTableName();

    int getFuncId();

    IDbQueryGenerator getDbQueryGenerator();
}
