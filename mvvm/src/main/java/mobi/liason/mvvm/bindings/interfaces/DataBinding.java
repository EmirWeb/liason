package mobi.liason.mvvm.bindings.interfaces;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public interface DataBinding extends Binding {
    public void onBind(final Context context, final Cursor cursor);
}
