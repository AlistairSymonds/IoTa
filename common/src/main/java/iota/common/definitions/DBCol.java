package iota.common.definitions;

/**
 * Created by alist on 8/07/2017.
 */
class DBCol {


    private String colName;
    private String colType;
    public DBCol(String colType, String colName){
        this.colType = colType;
        this.colName = colName;
    }
    public String getColName() {
        return colName;
    }

    public String getColType() {
        return colType;
    }

}
