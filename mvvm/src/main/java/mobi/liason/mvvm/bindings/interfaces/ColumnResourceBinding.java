package mobi.liason.mvvm.bindings.interfaces;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

import mobi.liason.mvvm.database.ViewModelColumn;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public interface ColumnResourceBinding extends ColumnBinding, ResourceBinding, Binding {
    public void onBind(final Context context, final View view, final int resourceId, final ViewModelColumn viewModelColumn, final Object value);
}
