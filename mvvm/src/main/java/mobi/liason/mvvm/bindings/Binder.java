package mobi.liason.mvvm.bindings;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

import mobi.liason.mvvm.bindings.interfaces.ColumnResourceBinding;
import mobi.liason.mvvm.bindings.interfaces.DataBinding;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class Binder implements ColumnResourceBinding, DataBinding {

    private final Set<Integer> mResourceIds = new HashSet<Integer>();
    ;
    private final Set<String> mColumnNames = new HashSet<String>();
    ;

    public Binder(final int resourceId) {
        mResourceIds.add(resourceId);
    }

    public Binder(final String columnName) {
        if (columnName != null) {
            mColumnNames.add(columnName);
        }
    }

    public Binder(final int resourceId, final String columnName) {
        mResourceIds.add(resourceId);
        if (columnName != null) {
            mColumnNames.add(columnName);
        }

    }

    public Binder(final Set<Integer> resourceIds, final Set<String> columnNames) {
        if (resourceIds != null) {
            mResourceIds.addAll(resourceIds);
        }

        if (columnNames != null) {
            mColumnNames.addAll(columnNames);
        }
    }

    @Override
    public Set<String> getColumnNames() {
        return mColumnNames;
    }

    @Override
    public Set<Integer> getResourceIds() {
        return mResourceIds;
    }

    @Override
    public void onBind(Context context, Cursor cursor, View view, int resourceId, int columnIndex, String columnName) {

    }

    @Override
    public void onBind(Context context, Cursor cursor, int columnIndex, String columnName) {

    }

    @Override
    public void onBind(Context context, Cursor cursor) {

    }

    @Override
    public void onBind(Context context, Cursor cursor, View view, int resourceId) {

    }

    @Override
    public void onBindStart(Context context) {

    }

    @Override
    public void onBindEnd(Context context) {

    }
}
