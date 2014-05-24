package mobi.liason.mvvm.bindings;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

import mobi.liason.mvvm.bindings.interfaces.ColumnResourceBinding;
import mobi.liason.mvvm.bindings.interfaces.DataBinding;
import mobi.liason.mvvm.database.ViewModelColumn;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class Binder implements ColumnResourceBinding, DataBinding {

    private final Set<Integer> mResourceIds = new HashSet<Integer>();
    private final Set<ViewModelColumn> mViewModelColumns = new HashSet<ViewModelColumn>();

    public Binder() {
    }

    public Binder(final int resourceId) {
        mResourceIds.add(resourceId);
    }

    public Binder(final ViewModelColumn viewModelColumn) {
        if (viewModelColumn != null) {
            mViewModelColumns.add(viewModelColumn);
        }
    }

    public Binder(final int resourceId, final ViewModelColumn viewModelColumn) {
        mResourceIds.add(resourceId);
        if (viewModelColumn != null) {
            mViewModelColumns.add(viewModelColumn);
        }
    }

    public Binder(final Set<Integer> resourceIds, final Set<ViewModelColumn> viewModelColumns) {
        if (resourceIds != null) {
            mResourceIds.addAll(resourceIds);
        }

        if (viewModelColumns != null) {
            mViewModelColumns.addAll(viewModelColumns);
        }
    }

    @Override
    public Set<ViewModelColumn> getViewModelColumns() {
        return mViewModelColumns;
    }

    @Override
    public void onBind(Context context, ViewModelColumn viewModelColumn, Object value) {

    }

    @Override
    public Set<Integer> getResourceIds() {
        return mResourceIds;
    }

    @Override
    public void onBind(Context context, View view, int resourceId) {

    }

    @Override
    public void onBindStart(Context context) {

    }

    @Override
    public void onBindEnd(Context context) {

    }

    @Override
    public void onBind(Context context, View view, int resourceId, ViewModelColumn viewModelColumn, Object value) {

    }

    @Override
    public void onBind(Context context, Cursor cursor) {

    }
}
