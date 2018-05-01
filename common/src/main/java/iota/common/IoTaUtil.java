package iota.common;




import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
        if (bytes.length != 8) {
            throw new IllegalArgumentException("Array of bytes must be 8 long, recieved len = " + bytes.length);
        }
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

    public static byte[] int2Bytes(int n) {
        byte b[] = new byte[4];
        for (int i = 0; i < 4; i++) {
            int shiftnum = (3 - i) * 8;
            b[i + 10] = (byte) (n >> shiftnum);
        }
        return b;
    }

    public static short bytes2Shot(byte bytes[]) {
        short val = 0;
        for (int i = 0; i < 2; i++) {
            short shiftnum = (short) ((1 - i) * 8);
            val = (short) (val | ((bytes[i] & 0xFF) << shiftnum));
        }
        return val;
    }

    public static byte[] short2Bytes(short n) {
        byte b[] = new byte[2];
        for (int i = 0; i < 2; i++) {
            int shiftnum = (1 - i) * 8;
            b[i] = (byte) (n >> shiftnum);
        }
        return b;
    }

    //from stack overflow
    //https://stackoverflow.com/questions/140131/convert-a-string-representation-of-a-hex-dump-to-a-byte-array-using-java
    public static byte[] hexStringToByteArray(String s) throws IllegalArgumentException {
        int len = s.length();
        s = s.toLowerCase();

        char[] validVals = getHexChars();
        for (int i = 0; i < len; i++) {
            boolean isValid = false;
            for (int j = 0; j < validVals.length && !isValid; j++) {
                if (s.charAt(i) == validVals[j]) {
                    isValid = true;
                }
            }

            if (!isValid) {
                String[] errorMsg = {s.substring(i, i), "isn't valid hex!"};
                throw new IllegalArgumentException(new Throwable(errorMsg[1]));
            }
        }


        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {


            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static HashMap createFlagMap(String args[]) {
        HashMap<String, String> flags = new HashMap<String, String>();

        int firstArg = 0;
        while (!args[firstArg].startsWith("-")) {
            firstArg++;
        }
        String flagKey = args[firstArg].substring(1).toLowerCase();
        String flagVal = "";

        for (int i = firstArg + 1; i < args.length; i++) {
            if (args[i].startsWith("-")) {
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


    public static char[] getHexChars() {
        char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        return chars;
    }

    public static List<Byte> byteArray2List(byte[] arr) {
        List<Byte> l = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            l.add(arr[i]);
        }
        return l;
    }


    public static long getHostId() {
        return 48;
    }

    public static String byteArr2HexStr(byte[] bytes) {
        StringBuilder ibyteStr = new StringBuilder("");
        for (int i = 0; i < bytes.length; i++) {
            ibyteStr.append("0x");
            ibyteStr.append(String.format("%02x ", bytes[i]));
        }
        return ibyteStr.toString();
    }
}
