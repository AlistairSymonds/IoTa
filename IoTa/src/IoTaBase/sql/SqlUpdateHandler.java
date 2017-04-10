package IoTaBase.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SqlUpdateHandler implements Runnable {
	private String sqlURL;
	private String sqlUser;
	private String sqlPassword;
	
	private ConcurrentLinkedQueue<String> updates;
	private ConcurrentLinkedQueue<Connection> conns;
	public boolean stop = false;
	
	public SqlUpdateHandler(String urlIn, String userIn, String passwordIn) throws SQLException{
		sqlURL = "jdbc:mysql://" + urlIn + ":3306/test";
		sqlUser = userIn;
		sqlPassword = passwordIn;
		System.out.println(sqlURL);
		updates = new ConcurrentLinkedQueue<String>();
		conns = new ConcurrentLinkedQueue<Connection>();
		connectToDB();
		
	}
	
	private void connectToDB() throws SQLException{
		conns.add(DriverManager.getConnection(sqlURL, sqlUser, sqlPassword));
	}
	
	public synchronized void addUpdate(String query){
		updates.add(query);
		notify();
	}

	public synchronized void run() {
		while(!stop){
			while(!updates.isEmpty()){
				try {
					
					UpdateWorker upObj = new UpdateWorker(updates.poll(), conns.peek());
					Thread t = new Thread(upObj);
					t.start();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
		}
	}	
}
