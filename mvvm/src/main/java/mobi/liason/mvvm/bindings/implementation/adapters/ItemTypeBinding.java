package mobi.liason.mvvm.bindings.implementation.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

import mobi.liason.mvvm.bindings.implementation.Binding;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public class ItemTypeBinding {

    private final int mLayoutResourceId;
    private final Set<Integer> mResourceIds = new HashSet<Integer>();
    private final Set<Binding> mBindings;

    public ItemTypeBinding(final int layoutResourceId, final Set<Binding> bindings){
        mLayoutResourceId = layoutResourceId;
        mBindings = bindings;
        for (final Binding binding : bindings){
            final int[] resourceIds = binding.getResourceIds();
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
        for (final Binding binding : mBindings){
            binding.bind(context, view, cursor);
        }
    }
}
