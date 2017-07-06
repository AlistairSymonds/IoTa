package iota.server.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


class UpdateWorker implements Runnable{
	
	private String sqlURL;
	private String sqlUser;
	private String sqlPassword;
	private String update;
	
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	public UpdateWorker(String update, Connection conn) throws SQLException{
		this.conn = conn;
		this.update = update;

	}
	

	
	public void run() {
		try {
			stmt = conn.createStatement();
			stmt.execute(update);
			System.out.println(update);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
