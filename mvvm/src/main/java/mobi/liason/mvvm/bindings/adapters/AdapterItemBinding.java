package mobi.liason.mvvm.bindings.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

import mobi.liason.mvvm.bindings.interfaces.Binding;
import mobi.liason.mvvm.bindings.interfaces.ResourceBinding;
import mobi.liason.mvvm.bindings.ItemBinding;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public class AdapterItemBinding {

    private final int mLayoutResourceId;
    private final Set<Integer> mResourceIds = new HashSet<Integer>();
    private final Set<Binding> mBindings;

    public AdapterItemBinding(final int layoutResourceId){
        this(layoutResourceId, new HashSet<Binding>());
    }

    public AdapterItemBinding(final int layoutResourceId, final Binding binding){
        this(layoutResourceId, Sets.newHashSet(binding));
    }

    public AdapterItemBinding(final int layoutResourceId, final Set<Binding> bindings){
        mLayoutResourceId = layoutResourceId;
        mBindings = bindings;
        for (final Binding binding : bindings){
            extractResourceIds(binding);
        }
    }

    private void extractResourceIds(final Binding binding) {
        if (binding instanceof ResourceBinding) {
            final ResourceBinding resourceBinding = (ResourceBinding) binding;
            final Set<Integer> resourceIds = resourceBinding.getResourceIds();
            for (final int resourceId : resourceIds) {
                mResourceIds.add(resourceId);
            }
        }
    }

    public void addBinding(final Binding binding){
        mBindings.add(binding);
        extractResourceIds(binding);
    }

    public int getLayoutResourceId() {
        return mLayoutResourceId;
    }

    public Set<Integer> getResourceIds() {
        return mResourceIds;
    }

    public void bind(final Context context, final View view, final Cursor cursor) {
        ItemBinding.bind(context, mBindings, view, cursor);
    }
}
