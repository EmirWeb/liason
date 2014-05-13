package mobi.liason.mvvm.database;

import android.content.Context;

import java.util.List;

/**
 * Created by Emir Hasanbegovic on 13/05/14.
 */
public abstract class Model extends Content {

    private static final String CREATE = " CREATE TABLE IF EXISTS %s %s ; ";

    @Override
    public String getCreate(final Context context) {
        final String name = getName(context);
        final List<ModelColumn> modelColumns = getColumns(context);
        final String createColumns = createColumns(modelColumns);
        final String create = String.format(CREATE, name, createColumns);
        return create;
    }

    public static String createColumns(final List<ModelColumn> modelColumns) {
        final StringBuilder stringBuilder = new StringBuilder();
        final int size = modelColumns.size();
        for (int index = 0; index < size; index++ ){
            final ModelColumn modelColumn = modelColumns.get(index);
            final String columnName = modelColumn.getName();
            final ModelColumn.Type type = modelColumn.getType();
            final String typeString = ModelColumn.Type.getSqlType(type);
            stringBuilder.append(columnName);
            stringBuilder.append(' ');
            stringBuilder.append(typeString);

            if (index != size -1) {
                stringBuilder.append(',');
            }
        }
        return stringBuilder.toString();
    }
}
