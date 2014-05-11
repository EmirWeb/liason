package mobi.liason.mvvm.bindings.implementation.databound;

import android.database.Cursor;
import android.widget.CursorAdapter;

import mobi.liason.mvvm.bindings.implementation.adapters.AdapterBinding;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public class DataBoundAdapterBinding extends AdapterBinding {

    private final String mTypeColumnName;

    public DataBoundAdapterBinding(final CursorAdapter adapter, final String typeColumnName) {
        super(adapter);
        mTypeColumnName = typeColumnName;
    }

    public int getItemType(final Cursor cursor) {
        if (mTypeColumnName == null){
            return 0;
        }

        final int typeColumnIndex = cursor.getColumnIndex(mTypeColumnName);
        if (typeColumnIndex == -1){
            return 0;
        }

        final int type = cursor.getInt(typeColumnIndex);
        return type;
    }

}
