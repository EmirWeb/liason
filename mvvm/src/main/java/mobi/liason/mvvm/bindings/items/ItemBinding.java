package mobi.liason.mvvm.bindings.items;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.view.View;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

import mobi.liason.mvvm.bindings.interfaces.Binding;
import mobi.liason.mvvm.bindings.interfaces.ColumnBinding;
import mobi.liason.mvvm.bindings.interfaces.ColumnResourceBinding;
import mobi.liason.mvvm.bindings.interfaces.DataBinding;
import mobi.liason.mvvm.bindings.interfaces.ResourceBinding;
import mobi.liason.mvvm.database.ViewModelColumn;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public class ItemBinding {

    private final View mRootView;
    private final Set<Binding> mBindings;

    public ItemBinding(final Fragment fragment, final int layoutResourceId){
        this(fragment.getView().findViewById(layoutResourceId), new HashSet<Binding>());
    }

    public ItemBinding(final Fragment fragment, final int layoutResourceId, final Binding binding){
        this(fragment.getView().findViewById(layoutResourceId), Sets.newHashSet(binding));
    }

    public ItemBinding(final Activity activity, final int layoutResourceId){
        this(activity.findViewById(layoutResourceId), new HashSet<Binding>());
    }

    public ItemBinding(final Activity activity, final int layoutResourceId, final Binding binding){
        this(activity.findViewById(layoutResourceId), Sets.newHashSet(binding));
    }

    public ItemBinding(final View rootView, final Set<Binding> bindings){
        mRootView = rootView;
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
                final View view = mRootView.findViewById(resourceId);
                mRootView.setTag(resourceId, view);
            }
        }
    }

    public void addBinding(final Binding binding){
        mBindings.add(binding);
        extractResourceIds(binding);
    }

    public void bind(final Context context, final View view, final Cursor cursor) {
        bind(context, mBindings, view, cursor);
    }

    public static void bind(final Context context, final Set<Binding> bindings, final View view, final Cursor cursor) {
        for (final Binding binding : bindings){

            binding.onBindStart(context);

            if (binding instanceof ResourceBinding) {
                final ResourceBinding resourceBinding = (ResourceBinding) binding;
                for (final Integer resourceId : resourceBinding.getResourceIds()) {
                    final View bindingView = (View) view.getTag(resourceId);
                    resourceBinding.onBind(context, bindingView, resourceId);

                    if (binding instanceof ColumnResourceBinding) {
                        final ColumnResourceBinding columnResourceBinding = (ColumnResourceBinding) binding;
                        for (final ViewModelColumn viewModelColumn : columnResourceBinding.getViewModelColumns()) {
                            final Object value = viewModelColumn.getValue(cursor);
                            columnResourceBinding.onBind(context, bindingView, resourceId, viewModelColumn, value);
                        }
                    }
                }
            }

            if (binding instanceof ColumnBinding) {
                final ColumnBinding columnBinding = (ColumnBinding) binding;
                for (final ViewModelColumn viewModelColumn : columnBinding.getViewModelColumns()) {
                    final Object value = viewModelColumn.getValue(cursor);
                    columnBinding.onBind(context, viewModelColumn, value);
                }
            }

            if (binding instanceof DataBinding) {
                final DataBinding dataBinding = (DataBinding) binding;
                dataBinding.onBind(context, cursor);
            }

            binding.onBindEnd(context);
        }
    }
}
