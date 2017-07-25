package iota.server.sql;

import iota.common.definitions.db.DbCol;
import iota.common.definitions.db.IDbQueryGenerator;
import iota.common.definitions.IFuncDef;


/**
 * Created by alist on 10/07/2017.
 */
class SqlQueries {


    public String getCreateTableStatement(IFuncDef definition) {
        String pk = "";
        StringBuilder stmt = new StringBuilder("CREATE TABLE ");
        stmt.append("'" + definition.getTableName() + "'(");
        for (DbCol col : definition.getCols()) {
            stmt.append("'" + col.getColName() + "'");
            stmt.append(" ");
            stmt.append(col.getColType().toUpperCase());

            if (col.getIsPrimaryKey()) {
                pk = col.getColName();
            }

            stmt.append(",\n");

        }
        stmt.append("PRIMARY KEY('" + pk + "));");

        return stmt.toString();
    }

    public String getCheckTableExistsStatement() {
        StringBuilder stmt = new StringBuilder("");

        return stmt.toString();
    }

    public String getCheckTableHasColumns() {
        return null;
    }


    public String getInsertDataQuery() {
        return null;
    }
}
