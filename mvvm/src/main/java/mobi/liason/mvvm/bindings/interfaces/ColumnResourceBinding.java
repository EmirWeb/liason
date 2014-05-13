package mobi.liason.mvvm.bindings.interfaces;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public interface ColumnResourceBinding extends ColumnBinding, ResourceBinding, Binding {
    public void onBind(final Context context, final Cursor cursor, final View view, final int resourceId, final int columnIndex, final String columnName);
}
