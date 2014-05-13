package mobi.liason.mvvm.bindings.adapters;

import android.database.Cursor;
import android.net.Uri;
import android.widget.CursorAdapter;

import java.util.ArrayList;
import java.util.List;

import mobi.liason.mvvm.bindings.BindDefinition;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public class AdapterBinding extends BindDefinition {

    private final List<ItemTypeBinding> mItemTypeBindings;
    private final CursorAdapter mAdapter;
    private final String mTypeColumnName;

    public AdapterBinding(final CursorAdapter adapter) {
        this(adapter, null, new ArrayList<ItemTypeBinding>());
    }

    public AdapterBinding(final CursorAdapter adapter, final String typeColumnName) {
        this(adapter, typeColumnName, new ArrayList<ItemTypeBinding>());
    }

    public AdapterBinding(final CursorAdapter adapter, final List<ItemTypeBinding> itemTypeBindings) {
        this(adapter, null, itemTypeBindings);
    }

    public AdapterBinding(final CursorAdapter adapter, final String typeColumnName, final List<ItemTypeBinding> itemTypeBindings) {
        mAdapter = adapter;
        mTypeColumnName = typeColumnName;
        mItemTypeBindings = new ArrayList<ItemTypeBinding>(itemTypeBindings);
    }

    public void addItemBinding(final ItemTypeBinding itemTypeBinding) {
        mItemTypeBindings.add(itemTypeBinding);
    }

    @Override
    public void onBind(final Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public Uri getUri() {
        return null;
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
