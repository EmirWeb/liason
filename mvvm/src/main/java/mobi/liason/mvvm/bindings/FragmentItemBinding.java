package mobi.liason.mvvm.bindings;

import android.app.Fragment;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

import mobi.liason.mvvm.bindings.interfaces.Binding;

/**
 * Created by Emir Hasanbegovic on 24/05/14.
 */
public abstract class FragmentItemBinding extends ItemBinding{

    public FragmentItemBinding(final Fragment fragment){
        super(fragment.getActivity().getApplicationContext(), fragment.getView(), new HashSet<Binding>());
    }

    public FragmentItemBinding(final Fragment fragment, final Binding binding){
        super(fragment.getActivity().getApplicationContext(), fragment.getView(), Sets.newHashSet(binding));
    }

    public FragmentItemBinding(final Fragment fragment, final Set<Binding> bindings){
        super(fragment.getActivity().getApplicationContext(), fragment.getView(), bindings);
    }

    public FragmentItemBinding(final Fragment fragment, final int layoutResourceId){
        super(fragment.getActivity().getApplicationContext(), fragment.getView().findViewById(layoutResourceId), new HashSet<Binding>());
    }

    public FragmentItemBinding(final Fragment fragment, final int layoutResourceId, final Binding binding){
        super(fragment.getActivity().getApplicationContext(), fragment.getView().findViewById(layoutResourceId), Sets.newHashSet(binding));
    }

    public FragmentItemBinding(final Fragment fragment, final int layoutResourceId, final Set<Binding> bindings){
        super(fragment.getActivity().getApplicationContext(), fragment.getView().findViewById(layoutResourceId), bindings);
    }

}
