package mobi.liason.mvvm.bindings.implementation.databound;

import android.content.Context;
import android.database.Cursor;

import java.util.Set;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public interface ResourceBinding extends DataBoundBinding{

    public Set<Integer> getResourceIds(final Context context);

    public void bind(final Context context, final Cursor cursor, final int resourceId);
}
