package iota.server.sql;

import com.mysql.cj.api.jdbc.Statement;
import com.mysql.cj.jdbc.MysqlDataSource;
import iota.common.definitions.DefinitionStore;
import iota.common.definitions.IFuncDef;

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

    protected boolean hasTable(IFuncDef def) {
        try{
            System.out.println("Checking for " + def.getTableName());
            ResultSet tableList = meta.getTables(null, null, def.getTableName(), null);
            while(tableList.next()){
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    protected void createTable(IFuncDef def) {
        try{
            Statement stmt = (Statement) dataSource.getConnection().createStatement();
            System.out.println("bleep bloop creating table: " + def.getTableName());
            //stmt.executeQuery("");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}
