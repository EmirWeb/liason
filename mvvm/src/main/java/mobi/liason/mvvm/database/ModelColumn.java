package mobi.liason.mvvm.database;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class ModelColumn extends Column{

    public ModelColumn(String sqlName, String name, Type type, String stringType) {
        super(sqlName, name, type, stringType);
    }

    public ModelColumn(String tableName, String name, Type type) {
        super(tableName, name, type);
    }

    public ModelColumn(String tableName, final ModelColumn modelColumn) {
        super(tableName, modelColumn.getName(), modelColumn.getType());
    }
}
