package mobi.liason.sample.overrides;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import mobi.liason.loaders.Path;
import mobi.liason.mvvm.task.Task;
import mobi.liason.mvvm.task.TaskService;
import mobi.liason.sample.R;
import mobi.liason.sample.product.tasks.ProductTask;
import mobi.liason.sample.products.tasks.ProductsTask;

/**
 * Created by Emir Hasanbegovic on 2014-05-20.
 */
public class SampleTaskService extends TaskService {

    public static void startTask(final Context context, final Uri uri) {
        startTask(context, uri, SampleTaskService.class);
    }

    public static void forceStartTask(final Context context, final Uri uri) {
        forceStartTask(context, uri, SampleTaskService.class);
    }

    @Override
    public String getAuthority(final Context context) {
        final Resources resources = context.getResources();
        return resources.getString(R.string.authority);
    }

    @Override
    public Map<Path, Class> getPathTaskMap(Context context) {
        final Map<Path, Class> mPathClassMap = new HashMap<Path, Class>();
        mPathClassMap.put(ProductsTask.Paths.PRODUCTS, ProductsTask.class);
        mPathClassMap.put(ProductTask.Paths.PRODUCT, ProductTask.class);
        return mPathClassMap;
    }
}
