package iota.common.definitions;

import iota.common.definitions.db.DbCol;
import iota.common.definitions.db.IDbQueryGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alist on 10/07/2017.
 */
class FileBasedFuncDef implements IFuncDef {
    private ArrayList<DbCol> cols;

    public FileBasedFuncDef(HashMap<String, List<String>> lineMap) {

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
    public int getFuncId() {
        return 0;
    }

    @Override
    public IDbQueryGenerator getDbQueryGenerator() {
        return null;
    }
}
