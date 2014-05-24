package mobi.liason.mvvm.bindings.adapters;

import android.app.Activity;
import android.widget.AdapterView;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import mobi.liason.mvvm.database.ViewModelColumn;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public abstract class ActivityAdapterBinding extends AdapterBinding {

    public ActivityAdapterBinding(final Activity activity, int resourceId) {
        this(activity, resourceId, null, new ArrayList<AdapterItemBinding>());
    }

    public ActivityAdapterBinding(final Activity activity, int resourceId, AdapterItemBinding adapterItemBinding) {
        this(activity, resourceId, null, Lists.newArrayList(adapterItemBinding));
    }

    public ActivityAdapterBinding(final Activity activity, int resourceId, List<AdapterItemBinding> adapterItemBindings) {
        this(activity, resourceId, null, adapterItemBindings);
    }

    public ActivityAdapterBinding(final Activity activity, int resourceId, final ViewModelColumn viewModelColumn) {
        this(activity, resourceId, viewModelColumn, new ArrayList<AdapterItemBinding>());
    }

    public ActivityAdapterBinding(final Activity activity, int resourceId, final ViewModelColumn viewModelColumn, AdapterItemBinding adapterItemBinding) {
        this(activity, resourceId, viewModelColumn, Lists.newArrayList(adapterItemBinding));
    }

    public ActivityAdapterBinding(final Activity activity, int resourceId, final ViewModelColumn viewModelColumn, List<AdapterItemBinding> adapterItemBindings) {
        super(activity.getApplicationContext(), (AdapterView) activity.findViewById(resourceId), viewModelColumn, adapterItemBindings);
    }

}
