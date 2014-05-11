package mobi.liason.mvvm.bindings.implementation;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

/**
 * Created by Emir Hasanbegovic on 29/04/14.
 */
public interface Binding {
    public void bind(final Context context, final View view, final Cursor cursor);
}
