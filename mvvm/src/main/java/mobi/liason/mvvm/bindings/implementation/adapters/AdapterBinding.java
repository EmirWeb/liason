package mobi.liason.mvvm.bindings.implementation.adapters;

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

    private final List<ItemTypeBinding> mItemTypeBindings = new ArrayList<ItemTypeBinding>();
    private final CursorAdapter mAdapter;

    public AdapterBinding(final CursorAdapter adapter) {
        mAdapter = adapter;
    }

    public void addItemBinding(final ItemTypeBinding itemTypeBinding){
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
        return 0;
    }

}
