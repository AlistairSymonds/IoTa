package iota.common.definitions;

import java.util.ArrayList;


public class TemperatureDef implements IDatabaseDef {

	public ArrayList<String> getColNames() {
		ArrayList<String> l = new ArrayList<String>();
		l.add("datapoint");
		l.add("time_in");
		l.add("device_id");
		l.add("value");
		return l;
	}
	
	public String getTableName() {
		return "temperature";
	}

	public int getFuncId() {
		return 16;
	}

	public String getInsertUpdate(long devId, int data) {
		ArrayList<String> cnames = getColNames();
		String q = "INSERT INTO "+ getTableName() + "(" +
				( cnames.get(1) + ", " + cnames.get(2) + ", " + cnames.get(3)+ 
				") VALUES (" 
				+ iota.common.IoTaUtil.time2DATETIME(System.currentTimeMillis()) +", "+ devId+", "+ data+")");
		return q;
	}

}
