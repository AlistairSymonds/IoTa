package iota.common.definitions;


import iota.common.definitions.db.DbCol;
import iota.common.definitions.db.IDbQueryGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alist on 10/07/2017.
 */
class FileBasedFuncDef implements IFuncDef {
    private ArrayList<DbCol> cols;
    private String name;
    private short id;

    public FileBasedFuncDef(HashMap<String, List<String>> lineMap) {
        cols = new ArrayList<DbCol>();
        if (lineMap.get("#name") != null) {
            this.name = lineMap.get("#name").get(0);
        } else {
            throw new IllegalArgumentException("Line map missing \"#name\" entry");
        }

        if (lineMap.get("#hexid") != null) {
            this.id = Short.parseShort(lineMap.get("#hexid").get(0), 16);
        } else {
            throw new IllegalArgumentException("Line map missing \"#hexid\" entry");
        }

        for (String line : lineMap.get("#dbcol")) {
            String[] splitLine = line.split(" ");
            String colName = splitLine[0];
            String colType = splitLine[1];
            String[] modifiers;
            if (splitLine.length > 2) {
                modifiers = Arrays.copyOfRange(splitLine, 2, splitLine.length);
            } else {
                modifiers = new String[0];
            }
            cols.add(new DbCol(colName, colType, modifiers));
        }
    }

    @Override
    public List<DbCol> getCols() {
        return null;
    }

    @Override
    public String getTableName() {
        return this.name;
    }

    @Override
    public short getFuncId() {
        return id;
    }
}
