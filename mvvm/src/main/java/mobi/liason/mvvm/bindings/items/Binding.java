package mobi.liason.mvvm.bindings.items;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import mobi.liason.loaders.BindDefinition;
import mobi.liason.mvvm.bindings.adapters.BindingCursorAdapter;
import mobi.liason.mvvm.bindings.adapters.ItemTypeBinding;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public abstract class Binding extends BindDefinition {

    private final List<ItemBinding> mItemTypeBindings;

    public Binding(final Context context, final View rootView, final int resourceId, final ItemBinding itemTypeBinding) {
        this(context, rootView, resourceId, null, Lists.newArrayList(itemTypeBinding));
    }

    public Binding(final Context context, final View rootView, final int resourceId, final String typeColumnName, final ItemBinding itemTypeBinding) {
        this(context, rootView, resourceId, typeColumnName, Lists.newArrayList(itemTypeBinding));
    }

    public Binding(final Context context, final View rootView, final int resourceId) {
        this(context, rootView, resourceId, null, new ArrayList<ItemBinding>());
    }

    public Binding(final Context context, final View rootView, final int resourceId, final String typeColumnName) {
        this(context, rootView, resourceId, typeColumnName, new ArrayList<ItemBinding>());
    }

    public Binding(final Context context, final View rootView, final int resourceId, final List<ItemBinding> itemTypeBindings) {
        this(context, rootView, resourceId, itemTypeBindings);
    }

    public Binding(final Context context, final View adapterView) {
        this(context, adapterView, new ArrayList<ItemBinding>());
    }

    public Binding(final Context context, final View view, final List<ItemBinding> itemTypeBindings) {
        super(context);
        mItemTypeBindings = new ArrayList<ItemBinding>(itemTypeBindings);
    }


    public Binding(final Context context, final Activity rootView, final int resourceId) {
        this(context, rootView.findViewById(resourceId), new ArrayList<ItemBinding>());
    }

    public Binding(final Context context, final Activity rootView, final int resourceId, final List<ItemBinding> itemTypeBindings) {
        this(context, rootView.findViewById(resourceId), itemTypeBindings);
    }

    public void addItemBinding(final ItemBinding itemTypeBinding) {
        mItemTypeBindings.add(itemTypeBinding);
    }

    public void setItemBindings(final List<ItemBinding> itemTypeBindings) {
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
