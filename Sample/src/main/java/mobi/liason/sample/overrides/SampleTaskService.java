package mobi.liason.sample.overrides;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mobi.liason.loaders.Path;
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
        return SampleProvider.getProviderAuthority(context);
    }

    @Override
    public Set<Class> getTasks(Context context) {
        final Set<Class> tasks = new HashSet<Class>();
        tasks.add(ProductsTask.class);
        tasks.add(ProductTask.class);
        return tasks;
    }

}
