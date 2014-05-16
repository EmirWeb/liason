package mobi.liason.mvvm.bindings.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public class BindingCursorAdapter extends CursorAdapter {

    private final Context mContext;
    private final AdapterBinding mAdapterBinding;

    public BindingCursorAdapter(final Context context, final AdapterBinding adapterBinding) {
        this(context, null, adapterBinding, false);
    }

    public BindingCursorAdapter(final Context context, final AdapterBinding adapterBinding, final boolean autoRequery) {
        this(context, null, null, autoRequery);
    }

    public BindingCursorAdapter(final Context context, final AdapterBinding adapterBinding, final int flags) {
        this(context, null, null, flags);
    }

    public BindingCursorAdapter(final Context context, final Cursor cursor, final AdapterBinding adapterBinding) {
        this(context, cursor, adapterBinding, false);
    }

    public BindingCursorAdapter(final Context context, final Cursor cursor, final AdapterBinding adapterBinding, final boolean autoRequery) {
        super(context, cursor, autoRequery);
        mContext = context;
        mAdapterBinding = adapterBinding;

    }

    public BindingCursorAdapter(final Context context, final Cursor cursor, final AdapterBinding adapterBinding, final int flags) {
        super(context, cursor, flags);
        mContext = context;
        mAdapterBinding = adapterBinding;
    }

    private static void optimize(final View view, final int resourceId) {
        view.setTag(resourceId, view.findViewById(resourceId));
    }

    private static void bindTextView(final View view, final int resourceId, final Cursor cursor, final String columnName) {
        final int columnIndex = cursor.getColumnIndex(columnName);
        final String string = cursor.getString(columnIndex);
        final TextView textView = (TextView) view.getTag(resourceId);
        textView.setText(string);
    }

    private ItemTypeBinding getItemTypeBinding(final Cursor cursor) {
        final int type = getItemViewType(cursor);
        return mAdapterBinding.getItemTypeBinding(type);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ItemTypeBinding itemTypeBinding = getItemTypeBinding(cursor);
        final int layoutResourceId = itemTypeBinding.getLayoutResourceId();
        final View rootView = layoutInflater.inflate(layoutResourceId, parent, false);

        final Set<Integer> resourceIds = itemTypeBinding.getResourceIds();
        for (final Integer resourceId : resourceIds) {
            optimize(rootView, resourceId);
        }
        return rootView;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final Cursor cursor = (Cursor) getItem(position);
        final View view = getView(convertView, parent, cursor);
        bindView(view, mContext, cursor);
        return view;
    }

    private View getView(final View convertView, final ViewGroup parent, final Cursor cursor) {
        if (convertView == null) {
            return newView(mContext, cursor, parent);
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return mAdapterBinding.getItemTypeCount();
    }

    private int getItemViewType(final Cursor cursor) {
        return mAdapterBinding.getItemType(cursor);
    }

    @Override
    public int getItemViewType(final int position) {
        final Cursor cursor = (Cursor) getItem(position);
        return getItemViewType(cursor);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final ItemTypeBinding itemTypeBinding = getItemTypeBinding(cursor);
        itemTypeBinding.bind(context, view, cursor);
    }

}