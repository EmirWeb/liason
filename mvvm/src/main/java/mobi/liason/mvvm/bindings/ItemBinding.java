package mobi.liason.mvvm.bindings;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

/**
 * Created by Emir Hasanbegovic on 29/04/14.
 */
public class ItemBinding {
    private final int[] mResourceIds;

    public ItemBinding (final int[] resourceIds){
        mResourceIds = resourceIds;
    }

    public int[] getResourceIds() {
        return mResourceIds;
    }

    public void bind(final Context context, final View view, final Cursor cursor) {

    }
}
