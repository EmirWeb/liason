package mobi.liason.mvvm.database;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class ForeignKeyModelColumn extends ModelColumn{

    private final String mForeignSqlName;

    public ForeignKeyModelColumn(final String sqlName, final ModelColumn modelColumn) {
        super(sqlName, modelColumn.getName(), modelColumn.getType());
        mForeignSqlName = modelColumn.getSqlName();
    }

    public ForeignKeyModelColumn(final String sqlName, final String name, final Type type, final String stringType, final String foreignSqlName) {
        super(sqlName, name, type, stringType);
        mForeignSqlName = foreignSqlName;
    }

    public ForeignKeyModelColumn(final String tableName, final String name, final Type type, final String foreignSqlName) {
        super(tableName, name, type);
        mForeignSqlName = foreignSqlName;
    }

    public String getForeignSqlName(){
        return mForeignSqlName;
    }
}
