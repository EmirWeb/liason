package mobi.liason.mvvm.bindings;

import android.app.Activity;
import android.app.Fragment;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

import mobi.liason.mvvm.bindings.interfaces.Binding;

/**
 * Created by Emir Hasanbegovic on 24/05/14.
 */
public abstract class ActivityItemBinding extends ItemBinding{

    public ActivityItemBinding(final Activity activity, final int layoutResourceId){
        super(activity.getApplicationContext(), activity.findViewById(layoutResourceId), new HashSet<Binding>());
    }

    public ActivityItemBinding(final Activity activity, final int layoutResourceId, final Binding binding){
        super(activity.getApplicationContext(), activity.findViewById(layoutResourceId), Sets.newHashSet(binding));
    }

    public ActivityItemBinding(final Activity activity, final int layoutResourceId, final Set<Binding> bindings){
        super(activity.getApplicationContext(), activity.findViewById(layoutResourceId), bindings);
    }

    public ActivityItemBinding(final Activity activity){
        super(activity.getApplicationContext(), activity.findViewById(android.R.id.content), new HashSet<Binding>());
    }

    public ActivityItemBinding(final Activity activity, final Binding binding){
        super(activity.getApplicationContext(), activity.findViewById(android.R.id.content), Sets.newHashSet(binding));
    }

    public ActivityItemBinding(final Activity activity, final Set<Binding> bindings){
        super(activity.getApplicationContext(), activity.findViewById(android.R.id.content), bindings);
    }

}
