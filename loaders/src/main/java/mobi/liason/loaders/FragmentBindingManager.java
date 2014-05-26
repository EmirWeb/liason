package mobi.liason.loaders;

import android.app.Activity;
import android.app.Fragment;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emir Hasanbegovic on 25/05/14.
 */
public class FragmentBindingManager extends ActivityBindingManager {
    public FragmentBindingManager(final Fragment fragment) {
        this(fragment, new ArrayList<BindDefinition>());
    }

    public FragmentBindingManager(final Fragment fragment, BindDefinition bindDefinition) {
        this(fragment, Lists.newArrayList(bindDefinition));
    }

    public FragmentBindingManager(final Fragment fragment, List<BindDefinition> bindDefinitions) {
        super(fragment.getActivity(), bindDefinitions);
    }
}
