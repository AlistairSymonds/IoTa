package iota.server.sql;

import com.mysql.cj.api.jdbc.Statement;
import com.mysql.cj.jdbc.MysqlDataSource;
import iota.common.definitions.IDatabaseDef;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Alistair on 7/07/2017.
 */
class DbInitialiser {
    private MysqlDataSource dataSource;
    private DatabaseMetaData meta;

    public DbInitialiser (MysqlDataSource dataSource){
        this.dataSource = dataSource;
        try{
            meta = dataSource.getConnection().getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    protected boolean hasTable(String tableName){
        try{
            ResultSet tableList = meta.getTables(null,null,tableName,null);
            while(tableList.next()){
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }



    protected void createTable(IDatabaseDef def){
        try{
            Statement stmt = (Statement) dataSource.getConnection().createStatement();
            stmt.executeQuery("");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}
