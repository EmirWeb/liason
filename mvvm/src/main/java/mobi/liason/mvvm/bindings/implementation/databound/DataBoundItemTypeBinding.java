package mobi.liason.mvvm.bindings.implementation.databound;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

import java.util.Set;

import mobi.liason.mvvm.bindings.implementation.Binding;
import mobi.liason.mvvm.bindings.implementation.adapters.ItemTypeBinding;

/**
 * Created by Emir Hasanbegovic on 2014-05-10.
 */
public class DataBoundItemTypeBinding extends ItemTypeBinding {

    public DataBoundItemTypeBinding(final int layoutResourceId, final Set<Binding> bindings) {
        super(layoutResourceId, bindings);
    }

    @Override
    public void bind(final Context context, final View view, final Cursor cursor) {
    }
}
