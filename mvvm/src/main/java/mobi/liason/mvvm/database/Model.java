package mobi.liason.mvvm.database;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import mobi.liason.loaders.Content;

/**
 * Created by Emir Hasanbegovic on 13/05/14.
 */
public abstract class Model extends Content {

    private static final String CREATE = "CREATE TABLE IF NOT EXISTS %s ( %s );";
    private static final String DROP = "DROP TABLE IF EXISTS %s;";
    private static final int VERSION = -1;

    @Override
    public String getCreate(final Context context) {
        final String name = getName(context);
        final List<Column> columns = getColumns(context);
        final String createColumns = createColumns(columns);
        final String create = String.format(CREATE, name, createColumns);
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

    public List<Column> getColumns(Context context) {
        return new ArrayList<Column>();
    }

    public static String createColumns(final List<Column> columns) {
        final StringBuilder stringBuilder = new StringBuilder();
        final int size = columns.size();
        for (int index = 0; index < size; index++ ){
            final Column modelColumn = columns.get(index);
            final String columnName = modelColumn.getName();
            final ModelColumn.Type type = modelColumn.getType();
            final String typeString = ModelColumn.Type.getSqlType(type);
            stringBuilder.append(columnName);
            stringBuilder.append(' ');
            stringBuilder.append(typeString);

            if (index != size -1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }
}
