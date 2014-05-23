package mobi.liason.sample.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.IBinder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import mobi.liason.mvvm.providers.Path;

/**
 * Created by Emir Hasanbegovic on 2014-05-20.
 */
public abstract class TaskService extends Service {

    private static final int THREAD_POOL_SIZE = 4;
    private static final ScheduledThreadPoolExecutor mSheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE);
    private final UriMatcher mURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final Map<Integer, Class> mCodeTaskClassMap = new HashMap<Integer, Class>();

    public static void startTask(final Context context, final Uri uri) {
        final String uriString = uri.toString();
        final Intent intent = new Intent(context, TaskService.class);
        intent.putExtra(EXTRAS.URI, uriString);
        context.startService(intent);
    }

    public abstract String getAuthority(final Context context);

    public abstract Map<Path, Class> getPathTaskMap(final Context context);

    @Override
    public void onCreate() {
        final Context context = getApplicationContext();
        final String authority = getAuthority(context);

        int index = 0;
        final Map<Path, Class> pathTaskMap = getPathTaskMap(context);
        final Set<Path> paths = pathTaskMap.keySet();
        for (final Path path : paths) {
            final String mathcerPath = path.getMatcherPath();
            final Class klass = pathTaskMap.get(path);
            mURIMatcher.addURI(authority, mathcerPath, index);
            mCodeTaskClassMap.put(index, klass);
            index++;
        }
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleOnStart(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void handleOnStart(Intent intent) {
        if (intent == null)
            return;

        final String uriString = intent.getStringExtra(EXTRAS.URI);
        final Uri uri = Uri.parse(uriString);
        final int code = mURIMatcher.match(uri);
        final Context context = getApplicationContext();
        final Task task = getTask(context, uri, code);
        if (task == null) {
            return;
        }
        mSheduledThreadPoolExecutor.execute(task);
    }

    private static Task getTask(final Context context, final Uri uri, final int code) {
        final Class klass = mCodeTaskClassMap.get(code);
        return createTask(context, uri, klass);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private static final class EXTRAS {
        public static final String URI = "uri";
    }

    public static <T extends Task> T createTask(final Context context, final Uri uri, final Class<T> klass) {
        try {
            final Constructor constructor = klass.getDeclaredConstructor(Context.class, Uri.class);
            constructor.setAccessible(true);
            return (T) constructor.newInstance(context, uri);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
