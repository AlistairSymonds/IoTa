package iota.server.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import iota.server.*;


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
		IoTaBaseMain.logger.log(Level.INFO, "Connecting to SQL " + sqlURL);
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
		          IoTaBaseMain.logger.log(Level.SEVERE, e.getMessage(), e);
		        }
			}
			
			try {
				this.wait();
			} catch (InterruptedException e) {
	          IoTaBaseMain.logger.log(Level.SEVERE, e.getMessage(), e);
	        }
			
			
		}
	}	
}
