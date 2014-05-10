package mobi.liason.mvvm.bindings;

import android.database.Cursor;
import android.net.Uri;
import android.widget.CursorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public class AdapterBinding extends Binding {

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

    public int getViewTypeCount() {
        return mItemTypeBindings.size();
    }
}
