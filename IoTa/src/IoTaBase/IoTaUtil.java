package IoTaBase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import IoTaBase.definitions.*;

public class IoTaUtil {
	
	private static HashMap<Integer, IDatabaseDef> dataDefs;
	public static void initDataDefs(){
		dataDefs = new HashMap<Integer, IDatabaseDef>();
		TemperatureDef tdef = new TemperatureDef();
		dataDefs.put(tdef.getFuncId(), tdef);
		
	}
	
	public static String getInsertQ(int dataId, long devId, int data){
		return dataDefs.get(dataId).getInsertUpdate(devId, data);
	}
	
	public static String time2DATETIME(long time){
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date(time);
		return "\""+fmt.format(d)+"\"";
	}
	

	
	public static long bytes2Long(byte bytes[]){
		long val = 0;
		for(int i = 0; i < 8; i++){
			int shiftnum = (7-i)*8;
			val  = val | ((bytes[i]&0xFF) << shiftnum);
		}
		return val;
	}
	
	public static byte[] long2Bytes(long n){
		byte b[] = new byte[8];
		for(int i = 0; i < 8; i++){
			int shiftnum = (7-i)*8;
			b[i] = (byte)(n >> shiftnum);
		}
		return b;
	}
	
	public static int bytes2Int(byte bytes[]){
		int val = 0;
		for(int i = 0; i < 4; i++){
			int shiftnum = (3-i)*8;
			val  = val | ((bytes[i]&0xFF) << shiftnum);
		}
		return val;
	}
	
	public static byte[] int2Bytes(long n){
		byte b[] = new byte[4];
		for(int i = 0; i < 4; i++){
			int shiftnum = (3-i)*8;
			b[i+10] = (byte)(n >> shiftnum);
		}
		return b;
	}
	
}
