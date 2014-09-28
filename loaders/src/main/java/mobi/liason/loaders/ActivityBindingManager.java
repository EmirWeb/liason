package mobi.liason.loaders;

import android.app.Activity;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emir Hasanbegovic on 25/05/14.
 */
public class ActivityBindingManager extends BindingManager {
    public ActivityBindingManager(final Activity activity) {
        this(activity, new ArrayList<BindDefinition>());
    }

    public ActivityBindingManager(final Activity activity, BindDefinition bindDefinition) {
        this(activity, Lists.newArrayList(bindDefinition));
    }

    public ActivityBindingManager(final Activity activity, List<BindDefinition> bindDefinitions) {
        super(activity.getApplicationContext(), activity.getLoaderManager(), bindDefinitions);
    }
}
