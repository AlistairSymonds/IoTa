package iota.server.sql;

import com.mysql.cj.jdbc.MysqlDataSource;
import iota.server.IoTaBaseMain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;


public class SqlHandler implements Runnable {
	private ConcurrentLinkedQueue<String> queries;
	private MysqlDataSource dataSource;

	public boolean stop = false;

	public SqlHandler(String urlIn, String userIn, String passwordIn){
		dataSource = new MysqlDataSource();
		dataSource.setURL(urlIn);
		dataSource.setUser(userIn);
		dataSource.setPassword(passwordIn);
		dataSource.setDatabaseName("test");


		queries = new ConcurrentLinkedQueue<String>();

		
	}
	
	public synchronized void addUpdate(String query){
		queries.add(query);
		notifyAll();
	}

	private boolean initDb(){
		DbInitialiser dbInit = new DbInitialiser(this.dataSource);
		System.out.println("Has temperature: " + dbInit.hasTable("temperature"));
		System.out.println("Has blahblah: " + dbInit.hasTable("blahblah"));
		return true;
	}

	public synchronized void run() {


		initDb();
		while(!stop){
			while(!queries.isEmpty()){
				Statement stmt = null;
				try{
					stmt = dataSource.getConnection().createStatement();
					ResultSet res = stmt.executeQuery(queries.poll());
					stmt.close();
				} catch (SQLException e){
					e.printStackTrace();
				} finally {

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
