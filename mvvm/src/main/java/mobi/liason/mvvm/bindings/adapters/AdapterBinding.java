package mobi.liason.mvvm.bindings.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import mobi.liason.loaders.BindDefinition;
import mobi.liason.mvvm.database.ViewModelColumn;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public abstract class AdapterBinding extends BindDefinition {

    private final List<AdapterItemBinding> mAdapterItemBindings;
    private final BindingCursorAdapter mAdapter;
    private final ViewModelColumn mTypeViewModelColumn;
    private final AdapterView mAdapterView;

    public AdapterBinding(final Context context, final AdapterView adapterView) {
        this(context, adapterView, null, new ArrayList<AdapterItemBinding>());
    }

    public AdapterBinding(final Context context, final AdapterView adapterView, final AdapterItemBinding adapterItemBinding) {
        this(context, adapterView, null, Lists.newArrayList(adapterItemBinding));
    }

    public AdapterBinding(final Context context, final AdapterView adapterView, final List<AdapterItemBinding> adapterItemBindings) {
        this(context, adapterView, null, adapterItemBindings);
    }

    public AdapterBinding(final Context context, final View rootView, final int resourceId) {
        this(context, rootView, resourceId, null, new ArrayList<AdapterItemBinding>());
    }

    public AdapterBinding(final Context context, final View rootView, final int resourceId, final AdapterItemBinding adapterItemBinding) {
        this(context, rootView, resourceId, null, Lists.newArrayList(adapterItemBinding));
    }

    public AdapterBinding(final Context context, final View rootView, final int resourceId, final List<AdapterItemBinding> adapterItemBindings) {
        this(context, rootView, resourceId, null, adapterItemBindings);
    }

    public AdapterBinding(final Context context, final View rootView, final int resourceId, final ViewModelColumn viewModelColumn) {
        this(context, rootView, resourceId, viewModelColumn, new ArrayList<AdapterItemBinding>());
    }

    public AdapterBinding(final Context context, final View rootView, final int resourceId, final ViewModelColumn viewModelColumn, final AdapterItemBinding adapterItemBinding) {
        this(context, rootView, resourceId, viewModelColumn, Lists.newArrayList(adapterItemBinding));
    }

    public AdapterBinding(final Context context, final View rootView, final int resourceId, final ViewModelColumn viewModelColumn, final List<AdapterItemBinding> adapterItemBindings) {
        this(context, (AdapterView) rootView.findViewById(resourceId), viewModelColumn, adapterItemBindings);
    }

    public AdapterBinding(final Context context, final AdapterView adapterView, final ViewModelColumn viewModelColumn, final List<AdapterItemBinding> adapterItemBindings) {
        super(context);
        mAdapter = new BindingCursorAdapter(context, this);
        mTypeViewModelColumn = viewModelColumn;
        mAdapterItemBindings = new ArrayList<AdapterItemBinding>(adapterItemBindings);
        mAdapterView = adapterView;
        adapterView.setAdapter(mAdapter);
    }

    public void addItemBinding(final AdapterItemBinding adapterItemBinding) {
        mAdapterItemBindings.add(adapterItemBinding);
    }

    public void setItemBindings(final List<AdapterItemBinding> adapterItemBindings) {
        mAdapterItemBindings.clear();
        mAdapterItemBindings.addAll(adapterItemBindings);
    }

    @Override
    public void onBind(final Context context, final Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    public AdapterItemBinding getItemTypeBinding(final int type) {
        return mAdapterItemBindings.get(type);
    }

    public int getItemTypeCount() {
        return Math.max(mAdapterItemBindings.size(), 1);
    }

    public int getItemType(final Cursor cursor) {
        if (mTypeViewModelColumn == null) {
            return 0;
        }

        return (Integer) mTypeViewModelColumn.getValue(cursor);
    }
}
