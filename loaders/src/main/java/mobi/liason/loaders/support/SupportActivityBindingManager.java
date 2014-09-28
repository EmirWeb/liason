package mobi.liason.loaders.support;


import android.support.v4.app.FragmentActivity;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import mobi.liason.loaders.BindDefinition;

/**
 * Created by Emir Hasanbegovic on 25/05/14.
 */
public class SupportActivityBindingManager extends SupportBindingManager {
    public SupportActivityBindingManager(final FragmentActivity fragmentActivity) {
        this(fragmentActivity, new ArrayList<BindDefinition>());
    }

    public SupportActivityBindingManager(final FragmentActivity fragmentActivity, BindDefinition bindDefinition) {
        this(fragmentActivity, Lists.newArrayList(bindDefinition));
    }

    public SupportActivityBindingManager(final FragmentActivity fragmentActivity, List<BindDefinition> bindDefinitions) {
        super(fragmentActivity.getApplicationContext(), fragmentActivity.getSupportLoaderManager(), bindDefinitions);
    }
}
