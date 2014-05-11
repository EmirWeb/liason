package mobi.liason.mvvm.bindings.implementation.databound;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public interface ColumnResourceBinding extends ColumnBinding, ResourceBinding, DataBoundBinding {
    public void bind(final Context context, final Cursor cursor, final int resourceId, final int columnIndex, final String columnName);
}
