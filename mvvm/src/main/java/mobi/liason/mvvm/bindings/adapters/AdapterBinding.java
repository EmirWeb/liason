package mobi.liason.mvvm.bindings.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import mobi.liason.mvvm.bindings.BindDefinition;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public abstract class AdapterBinding extends BindDefinition {

    private final List<ItemTypeBinding> mItemTypeBindings;
    private final BindingCursorAdapter mAdapter;
    private final String mTypeColumnName;
    private final AdapterView<?> mAdapterView;

    public AdapterBinding(final Context context, final View rootView, final int resourceId, final ItemTypeBinding itemTypeBinding) {
        this(context, rootView, resourceId, null, Lists.newArrayList(itemTypeBinding));
    }

    public AdapterBinding(final Context context, final View rootView, final int resourceId, final String typeColumnName, final ItemTypeBinding itemTypeBinding) {
        this(context, rootView, resourceId, typeColumnName, Lists.newArrayList(itemTypeBinding));
    }

    public AdapterBinding(final Context context, final View rootView, final int resourceId) {
        this(context, rootView, resourceId, null, new ArrayList<ItemTypeBinding>());
    }

    public AdapterBinding(final Context context, final View rootView, final int resourceId, final String typeColumnName) {
        this(context, rootView, resourceId, typeColumnName, new ArrayList<ItemTypeBinding>());
    }

    public AdapterBinding(final Context context, final View rootView, final int resourceId, final List<ItemTypeBinding> itemTypeBindings) {
        this(context, rootView, resourceId, null, itemTypeBindings);
    }

    public AdapterBinding(final Context context, final View rootView, final int resourceId, final String typeColumnName, final List<ItemTypeBinding> itemTypeBindings) {
        this(context, (AbsListView) rootView.findViewById(resourceId), typeColumnName, itemTypeBindings);
    }

    public AdapterBinding(final Context context, final AbsListView adapterView) {
        this(context, adapterView, null, new ArrayList<ItemTypeBinding>());
    }

    public AdapterBinding(final Context context, final AbsListView adapterView, final String typeColumnName, final List<ItemTypeBinding> itemTypeBindings) {
        mAdapter = new BindingCursorAdapter(context, this);
        mTypeColumnName = typeColumnName;
        mItemTypeBindings = new ArrayList<ItemTypeBinding>(itemTypeBindings);
        adapterView.setAdapter(mAdapter);
        mAdapterView = adapterView;
    }


    public AdapterBinding(final Context context, final Activity rootView, final int resourceId) {
        this(context, (AbsListView) rootView.findViewById(resourceId), null, new ArrayList<ItemTypeBinding>());
    }

    public AdapterBinding(final Context context, final Activity rootView, final int resourceId, final String typeColumnName, final List<ItemTypeBinding> itemTypeBindings) {
        this(context, (AbsListView) rootView.findViewById(resourceId), typeColumnName, itemTypeBindings);
    }

    public void addItemBinding(final ItemTypeBinding itemTypeBinding) {
        mItemTypeBindings.add(itemTypeBinding);
    }

    public void setItemBindings(final List<ItemTypeBinding> itemTypeBindings) {
        mItemTypeBindings.clear();
        mItemTypeBindings.addAll(itemTypeBindings);
    }

    @Override
    public void onBind(final Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    public ItemTypeBinding getItemTypeBinding(final int type) {
        return mItemTypeBindings.get(type);
    }

    public int getItemTypeCount() {
        return Math.max(mItemTypeBindings.size(), 1);
    }

    public int getItemType(final Cursor cursor) {
        if (mTypeColumnName == null) {
            return 0;
        }

        final int typeColumnIndex = cursor.getColumnIndex(mTypeColumnName);
        if (typeColumnIndex == -1) {
            return 0;
        }

        final int type = cursor.getInt(typeColumnIndex);
        return type;
    }
}
