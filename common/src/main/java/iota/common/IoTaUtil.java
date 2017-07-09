package iota.common;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class IoTaUtil {
  private IoTaUtil() {
    throw new IllegalStateException("Util Class");
  }


  public static String time2DATETIME(long time) {
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date d = new Date(time);
    return "\"" + fmt.format(d) + "\"";
  }

  public static long bytes2Long(byte bytes[]) {
    long val = 0;
    for (int i = 0; i < 8; i++) {
      int shiftnum = (7 - i) * 8;
      val = val | ((bytes[i] & 0xFF) << shiftnum);
    }
    return val;
  }

  public static byte[] long2Bytes(long n) {
    byte b[] = new byte[8];
    for (int i = 0; i < 8; i++) {
      int shiftnum = (7 - i) * 8;
      b[i] = (byte) (n >> shiftnum);
    }
    return b;
  }

  public static int bytes2Int(byte bytes[]) {
    int val = 0;
    for (int i = 0; i < 4; i++) {
      int shiftnum = (3 - i) * 8;
      val = val | ((bytes[i] & 0xFF) << shiftnum);
    }
    return val;
  }

  public static byte[] int2Bytes(long n) {
    byte b[] = new byte[4];
    for (int i = 0; i < 4; i++) {
      int shiftnum = (3 - i) * 8;
      b[i + 10] = (byte) (n >> shiftnum);
    }
    return b;
  }

  public static HashMap createFlagMap(String args[]){
    HashMap<String, String> flags = new HashMap<String, String>();

    int firstArg = 0;
    while(!args[firstArg].startsWith("-")){
        firstArg++;
    }
    String flagKey = args[firstArg].substring(1).toLowerCase();;
    String flagVal ="";

    for(int i = firstArg+1; i < args.length; i++){
        if(args[i].startsWith("-")){
            flags.put(flagKey, flagVal);
            flagVal = "";
            flagKey = args[i].substring(1).toLowerCase();

        } else {
            flagVal = flagVal + args[i];
        }

    }
    flags.put(flagKey, flagVal);
    return flags;
  }

}
