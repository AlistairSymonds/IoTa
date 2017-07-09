import java.io.DataOutputStream;
import java.net.Socket;

public class Blaster {

	private static long devId = 64;
	
	private static byte[] fakeTempData(int t){
		//1 byte for length
		//8 bytes for device id
		//1 byte to signify temperature data next
		//4 bytes for temperature value
		byte[] test = new byte[14];
		test[0] = 14;
		for(int i = 0; i < 8; i++){
			int shiftnum = (7-i)*8;
			byte nextbyte = (byte)(devId >> shiftnum);
			test[i+1] = nextbyte;
		}
		test[9] = 16;
		for(int i = 0; i < 4; i++){
			int shiftnum = (3-i)*8;
			test[i+10] = (byte)(t >> shiftnum);
		}
		
		
		return test;
	}
	
	
	public static void testSpam() {
		long start = System.currentTimeMillis();
		for(int i = 0; i < 10000; i++){
			try (Socket sock = new Socket("localhost", 2812)) {
				DataOutputStream os = new DataOutputStream(sock.getOutputStream());
				os.write(fakeTempData(i));		
				sock.close();
				
			} catch (Exception e) {
				System.out.println("Couldn't Connect " + i);
			}
		}
		long total = System.currentTimeMillis() - start;
		System.out.println("That took " + total + "[ms]");
	}
}
