package iota.server.sql;

import iota.common.definitions.AbstractDbQueryGenerator;

import java.sql.Statement;

/**
 * Created by alist on 10/07/2017.
 */
public class SqlQueryGen extends AbstractDbQueryGenerator {
    @Override
    public Statement getCreatTableStatement() {
        return null;
    }
}
