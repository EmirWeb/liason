package mobi.liason.loaders.support;

import android.support.v4.app.Fragment;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import mobi.liason.loaders.ActivityBindingManager;
import mobi.liason.loaders.BindDefinition;

/**
 * Created by Emir Hasanbegovic on 25/05/14.
 */
public class SupportFragmentBindingManager extends ActivityBindingManager {
    public SupportFragmentBindingManager(final Fragment fragment) {
        this(fragment, new ArrayList<BindDefinition>());
    }

    public SupportFragmentBindingManager(final Fragment fragment, BindDefinition bindDefinition) {
        this(fragment, Lists.newArrayList(bindDefinition));
    }

    public SupportFragmentBindingManager(final Fragment fragment, List<BindDefinition> bindDefinitions) {
        super(fragment.getActivity(), bindDefinitions);
    }
}
