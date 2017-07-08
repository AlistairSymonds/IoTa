package iota.common.definitions;

import java.util.ArrayList;


class TemperatureDef implements IDatabaseDef {



	@Override
	public ArrayList<DBCol> getCols() {
		ArrayList<DBCol> l = new ArrayList<DBCol>();
		l.add(new DBCol("INT","datapoint"));
		l.add(new DBCol("datetime","time_in"));
		l.add(new DBCol("INT","device_id"));
		l.add(new DBCol("INT","value"));
		return l;
	}

	public String getTableName() {
		return "temperature";
	}

	public int getFuncId() {
		return 16;
	}

	public String getInsertUpdate(long devId, int data) {
		ArrayList<DBCol> cols = getCols();
		String q = "INSERT INTO "+ getTableName() + "(" +
				( cols.get(1).getColName() + ", " + cols.get(2).getColName() + ", " + cols.get(3).getColName()+
				") VALUES (" 
				+ iota.common.IoTaUtil.time2DATETIME(System.currentTimeMillis()) +", "+ devId+", "+ data+")");
		return q;
	}

}
