package iota.common.definitions.db;

import iota.common.definitions.IFuncDef;

import java.util.List;

public class Heartbeat implements IFuncDef {
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
        return 60;
    }
}
