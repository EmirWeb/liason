package mobi.liason.mvvm.bindings.interfaces;

import android.content.Context;
import android.database.Cursor;

import java.util.Set;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public interface ColumnBinding extends Binding {
    public Set<String> getColumnNames();
    public void onBind(final Context context, final Cursor cursor, final int columnIndex, final String columnName);
}
