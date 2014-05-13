package mobi.liason.mvvm.bindings.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

import mobi.liason.mvvm.bindings.interfaces.Binding;
import mobi.liason.mvvm.bindings.interfaces.ColumnBinding;
import mobi.liason.mvvm.bindings.interfaces.ColumnResourceBinding;
import mobi.liason.mvvm.bindings.interfaces.DataBinding;
import mobi.liason.mvvm.bindings.interfaces.ResourceBinding;

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
            if (binding instanceof ResourceBinding) {
                final ResourceBinding resourceBinding = (ResourceBinding) binding;
                final Set<Integer> resourceIds = resourceBinding.getResourceIds();
                for (final int resourceId : resourceIds) {
                    mResourceIds.add(resourceId);
                }
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

            binding.onBindStart(context);

            if (binding instanceof ResourceBinding) {
                final ResourceBinding resourceBinding = (ResourceBinding) binding;
                for (final Integer resourceId : resourceBinding.getResourceIds()) {
                    final View bindingView = (View) view.getTag(resourceId);
                    resourceBinding.onBind(context, cursor, bindingView, resourceId);

                    if (binding instanceof ColumnResourceBinding) {
                        final ColumnResourceBinding columnResourceBinding = (ColumnResourceBinding) binding;
                        for (final String columnName : columnResourceBinding.getColumnNames()) {
                            final int columnIndex = cursor.getColumnIndex(columnName);
                            columnResourceBinding.onBind(context, cursor, bindingView, resourceId, columnIndex, columnName);
                        }
                    }
                }
            }

            if (binding instanceof ColumnBinding) {
                final ColumnBinding columnBinding = (ColumnBinding) binding;
                for (final String columnName : columnBinding.getColumnNames()) {
                    final int columnIndex = cursor.getColumnIndex(columnName);
                    columnBinding.onBind(context, cursor, columnIndex, columnName);
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
