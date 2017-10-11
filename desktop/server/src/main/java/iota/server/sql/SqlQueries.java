package iota.server.sql;

import iota.common.definitions.IFuncDef;
import iota.common.db.DbCol;


/**
 * Created by alist on 10/07/2017.
 */
class SqlQueries {
    //private String schemaName;

    public static String getCreateTableStatement(IFuncDef definition, String schemaName) {
        String pk = "";
        StringBuilder stmt = new StringBuilder("CREATE TABLE ");
        stmt.append("'" + schemaName + "'.");
        stmt.append("'" + definition.getTableName() + "' ( \n");
        for (DbCol col : definition.getCols()) {
            stmt.append("'" + col.getColName() + "'");
            stmt.append(" ");
            stmt.append(col.getColType().toUpperCase());
            if (col.getIsPrimaryKey()) {
                pk = col.getColName();
            }

            if (col.getIsNotNull()) {
                stmt.append(" NOT");
            }
            stmt.append(" NULL");

            if (col.getIsAutoInc()) {
                stmt.append(" AUTO_INCREMENT");
            }

            stmt.append(",\n");

        }
        stmt.append("PRIMARY KEY('" + pk + "'));");

        /*return "CREATE TABLE `test`.`new_table` (\n" +
                "  `idnew_table` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `point` INT NULL,\n" +
                "  `new_tablecol` VARCHAR(45) NULL,\n" +
                "  PRIMARY KEY (`idnew_table`));\n";*/

        return stmt.toString().replace("'", "`");
    }

    public static String getCheckTableExistsStatement() {
        StringBuilder stmt = new StringBuilder("");

        return stmt.toString();
    }

    public static String getCheckTableHasColumns(IFuncDef def) {
        StringBuilder stmt = new StringBuilder("");

        return stmt.toString();
    }


    public String getInsertDataQuery(IFuncDef def, String schemaName) {
        return null;
    }
}
