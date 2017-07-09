package iota.common.definitions;

import java.sql.Statement;

/**
 * Created by alist on 10/07/2017.
 */
public abstract class AbstractDbQueryGenerator {
    public abstract Statement getCreatTableStatement();
}
