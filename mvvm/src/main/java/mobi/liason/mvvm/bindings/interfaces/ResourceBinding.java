package mobi.liason.mvvm.bindings.interfaces;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

import java.util.Set;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public interface ResourceBinding extends Binding {
    public Set<Integer> getResourceIds();
    public void onBind(final Context context, final Cursor cursor, final View view, final int resourceId);
}
