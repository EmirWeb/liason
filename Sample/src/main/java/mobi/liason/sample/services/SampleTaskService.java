package mobi.liason.sample.services;

import android.content.Context;
import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

import mobi.liason.mvvm.providers.Path;
import mobi.liason.sample.R;
import mobi.liason.sample.content.models.TaskStateTable;
import mobi.liason.sample.tasks.ProductTask;

/**
 * Created by Emir Hasanbegovic on 2014-05-20.
 */
public class SampleTaskService extends TaskService {

    @Override
    public String getAuthority(final Context context) {
        final Resources resources = context.getResources();
        return resources.getString(R.string.authority);
    }

    @Override
    public Map<Path, Class> getPathTaskMap(Context context) {
        final Map<Path, Class> mPathClassMap = new HashMap<Path, Class>();
        mPathClassMap.put(ProductTask.Paths.PRODUCTS, ProductTask.class);
        return mPathClassMap;
    }
}
