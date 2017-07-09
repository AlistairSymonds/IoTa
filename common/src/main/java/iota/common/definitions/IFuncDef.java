package iota.common.definitions;


import java.util.ArrayList;

public interface IFuncDef {
	public ArrayList<DBCol> getCols();
	public String getTableName();
	public int getFuncId();
	public String getInsertUpdate(long devId, int data);
}
