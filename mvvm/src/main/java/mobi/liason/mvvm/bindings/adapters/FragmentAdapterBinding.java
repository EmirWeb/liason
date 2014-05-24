package mobi.liason.mvvm.bindings.adapters;

import android.app.Fragment;
import android.widget.AdapterView;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import mobi.liason.mvvm.database.ViewModelColumn;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public abstract class FragmentAdapterBinding extends AdapterBinding {

    public FragmentAdapterBinding(final Fragment fragment, int resourceId) {
        this(fragment, resourceId, null, new ArrayList<AdapterItemBinding>());
    }

    public FragmentAdapterBinding(final Fragment fragment, int resourceId, AdapterItemBinding adapterItemBinding) {
        this(fragment, resourceId, null, Lists.newArrayList(adapterItemBinding));
    }

    public FragmentAdapterBinding(final Fragment fragment, int resourceId, List<AdapterItemBinding> adapterItemBindings) {
        this(fragment, resourceId, null, adapterItemBindings);
    }

    public FragmentAdapterBinding(final Fragment fragment, int resourceId, final ViewModelColumn viewModelColumn) {
        this(fragment, resourceId, viewModelColumn, new ArrayList<AdapterItemBinding>());
    }

    public FragmentAdapterBinding(final Fragment fragment, int resourceId, final ViewModelColumn viewModelColumn, AdapterItemBinding adapterItemBinding) {
        this(fragment, resourceId, viewModelColumn, Lists.newArrayList(adapterItemBinding));
    }

    public FragmentAdapterBinding(final Fragment fragment, int resourceId, final ViewModelColumn viewModelColumn, List<AdapterItemBinding> adapterItemBindings) {
        super(fragment.getActivity().getApplicationContext(), (AdapterView) fragment.getView().findViewById(resourceId), viewModelColumn, adapterItemBindings);
    }
}
