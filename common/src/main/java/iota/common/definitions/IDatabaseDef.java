package iota.common.definitions;


import java.util.ArrayList;

public interface IDatabaseDef {
	public ArrayList<String> getColNames();
	public String getTableName();
	public int getFuncId();
	public String getInsertUpdate(long devId, int data);
}
