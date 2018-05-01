package iota.server.sql;

import com.mysql.cj.jdbc.MysqlDataSource;
import iota.common.functions.IFunction;
import iota.server.IoTaBaseMain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;


public class SqlHandler implements Runnable {
    private ConcurrentLinkedQueue<String> queries;
    private MysqlDataSource dataSource;
    private SqlQueries queryGen;

    private boolean stop = false;

    public SqlHandler(String urlIn, String userIn, String passwordIn, String schemaName) {
        dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://" + urlIn + ":3306");
        dataSource.setUser(userIn);
        dataSource.setPassword(passwordIn);
        dataSource.setDatabaseName(schemaName);
        queries = new ConcurrentLinkedQueue<String>();
    }

    public synchronized void addUpdate(String query) {
        queries.add(query);
        notifyAll();
    }

    private boolean initDb() {
        DbInitialiser dbInit = new DbInitialiser(this.dataSource);
        for (IFunction def : IoTaBaseMain.defStore) {
            if (!dbInit.hasTable(def)) {
                dbInit.createTable(def);
            }
        }
        return true;
    }

    public synchronized void run() {
        initDb();
        while (!stop) {
            while (!queries.isEmpty()) {
                Statement stmt = null;
                try {
                    stmt = dataSource.getConnection().createStatement();
                    ResultSet res = stmt.executeQuery(queries.poll());
                    stmt.close();
                } catch (SQLException e) {
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

    public boolean test() {
        try {
            this.dataSource.getConnection();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public synchronized void requestStop() {
        this.stop = true;
    }
}
