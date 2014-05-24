package mobi.liason.mvvm.bindings.interfaces;

import android.content.Context;
import android.database.Cursor;

import java.util.Set;

import mobi.liason.mvvm.database.ViewModelColumn;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public interface ColumnBinding extends Binding {
    public Set<ViewModelColumn> getViewModelColumns();
    public void onBind(final Context context, final ViewModelColumn viewModelColumn, final Object value);
}
