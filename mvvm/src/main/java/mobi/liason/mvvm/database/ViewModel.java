package mobi.liason.mvvm.database;

import android.content.Context;

import java.util.List;

/**
 * Created by Emir Hasanbegovic on 13/05/14.
 */
public abstract class ViewModel extends Model{

    private static final String CREATE = " CREATE VIEW IF EXISTS %s AS SELECT %s FROM %s ; ";

    @Override
    public String getCreate(final Context context) {
        final String name = getName(context);
        final List<ModelColumn> modelColumns = getColumns(context);
        final String createColumns = createColumns(modelColumns);
        final String selection = getSelection(context);
        final String create = String.format(CREATE, name, createColumns, selection);
        return create;
    }

    protected abstract String getSelection(final Context context);

}
