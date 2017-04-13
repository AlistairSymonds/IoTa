package shortbase.inserters;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Queue;

public class TcpHandler implements Runnable{
	private Socket client;
	private Queue splitData;
	
	
	public TcpHandler(Socket sock){
		client = sock;
	}
	
	public void run() {
		
		try {
			DataInputStream in = new DataInputStream(client.getInputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
