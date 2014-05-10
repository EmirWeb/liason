package mobi.liason.mvvm.bindings;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public class ItemTypeBinding {

    private final int mLayoutResourceId;
    private final Set<Integer> mResourceIds = new HashSet<Integer>();
    private final Set<ItemBinding> mItemBindings;

    public ItemTypeBinding(final int layoutResourceId, final Set<ItemBinding> itemBindings){
        mLayoutResourceId = layoutResourceId;
        mItemBindings = itemBindings;
        for (final ItemBinding itemBinding : itemBindings){
            final int[] resourceIds = itemBinding.getResourceIds();
            for (final int resourceId : resourceIds){
                mResourceIds.add(resourceId);
            }
        }
    }

    public int getLayoutResourceId() {
        return mLayoutResourceId;
    }

    public Set<Integer> getResourceIds() {
        return mResourceIds;
    }

    public void bind(final Context context, final View view, final Cursor cursor) {
        for (final ItemBinding itemBinding : mItemBindings){
            itemBinding.bind(context, view, cursor);
        }
    }
}
