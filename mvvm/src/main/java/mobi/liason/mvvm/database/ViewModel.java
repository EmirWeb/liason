package mobi.liason.mvvm.database;

import android.content.Context;

import java.util.List;

/**
 * Created by Emir Hasanbegovic on 13/05/14.
 */
public abstract class ViewModel extends Model{

    private static final String CREATE = "CREATE VIEW IF NOT EXISTS %s AS SELECT %s FROM %s;";
    private static final String DROP = "DROP TABLE IF EXISTS %s;";
    private static final int VERSION = -1;

    @Override
    public String getCreate(final Context context) {
        final String name = getName(context);
        final List<Column> columns = getColumns(context);
        final String createColumns = createColumns(columns);
        final String selection = getSelection(context);
        final String create = String.format(CREATE, name, createColumns, selection);
        return create;
    }

    @Override
    public String getDrop(final Context context) {
        final String name = getName(context);
        final String drop = String.format(DROP, name);
        return drop;
    }

    @Override
    public int getVersion(final Context context) {
        return VERSION;
    }

    protected abstract String getSelection(final Context context);

    public static String createColumns(final List<Column> columns) {
        if (columns == null || columns.isEmpty()){
            return "*";
        }

        final StringBuilder stringBuilder = new StringBuilder();
        final int size = columns.size();
        for (int index = 0; index < size; index++ ){
            final Column modelColumn = columns.get(index);
            final String columnName = modelColumn.getColumnLine();
            stringBuilder.append(columnName);

            if (index != size -1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }
}
