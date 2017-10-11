package iota.common.db;

/**
 * Created by alist on 10/07/2017.
 */
public interface IDbQueryGenerator {
    String getCreateTableStatement();

    String getCheckTableExistsStatement();

    String getCheckTableHasColumns();

    String getInsertDataQuery();
}
