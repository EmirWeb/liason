package mobi.liason.mvvm.bindings.adapters;

import android.content.Context;
import android.database.Cursor;

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

    public AdapterBinding(final Context context, final ItemTypeBinding itemTypeBinding) {
        this(context, null, Lists.newArrayList(itemTypeBinding));
    }

    public AdapterBinding(final Context context, final String typeColumnName, final ItemTypeBinding itemTypeBinding) {
        this(context, typeColumnName, Lists.newArrayList(itemTypeBinding));
    }

    public AdapterBinding(final Context context) {
        this(context, null, new ArrayList<ItemTypeBinding>());
    }

    public AdapterBinding(final Context context, final String typeColumnName) {
        this(context, typeColumnName, new ArrayList<ItemTypeBinding>());
    }

    public AdapterBinding(final Context context, final List<ItemTypeBinding> itemTypeBindings) {
        this(context, null, itemTypeBindings);
    }

    public AdapterBinding(final Context context, final String typeColumnName, final List<ItemTypeBinding> itemTypeBindings) {
        mAdapter = new BindingCursorAdapter(context, this);
        mTypeColumnName = typeColumnName;
        mItemTypeBindings = new ArrayList<ItemTypeBinding>(itemTypeBindings);
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
        return mItemTypeBindings.size();
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
