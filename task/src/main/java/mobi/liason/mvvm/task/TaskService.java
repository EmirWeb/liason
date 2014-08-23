package mobi.liason.mvvm.task;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.IBinder;

import com.google.common.collect.Sets;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import mobi.liason.loaders.Path;
import mobi.liason.mvvm.database.annotations.PathDefinition;
import mobi.liason.mvvm.database.annotations.PathDefinitions;

/**
 * Created by Emir Hasanbegovic on 2014-05-20.
 */
public abstract class TaskService extends Service {

    private static final int THREAD_POOL_SIZE = 4;
    private static final ScheduledThreadPoolExecutor mSheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE);
    private final UriMatcher mURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final Map<Integer, Class> mCodeTaskClassMap = new HashMap<Integer, Class>();

    public abstract String getAuthority(final Context context);

    public abstract Set<Class> getTasks(final Context context);

    @Override
    public void onCreate() {
        final Context context = getApplicationContext();
        final String authority = getAuthority(context);

        int index = 0;
        final Set<Class> tasks = getTasks(context);
        for (final Class klass : tasks) {
            final Set<Path> paths = getPaths(klass);
            for (final Path path : paths) {
                final String matcherPath = path.getMatcherPath();
                mURIMatcher.addURI(authority, matcherPath, index);
                mCodeTaskClassMap.put(index, klass);
                index++;
            }
        }
        super.onCreate();
    }

    private Set<Path> getPaths(final Context context, final Task task) {
        return getPaths(task.getClass());
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
        final boolean forceTask = intent.getBooleanExtra(EXTRAS.FORCE_TASK, false);
        final Uri uri = Uri.parse(uriString);
        final int code = mURIMatcher.match(uri);
        final Context context = getApplicationContext();
        final String authority = getAuthority(context);
        final Task task = getTask(context, authority, uri, code);
        task.setForceTask(forceTask);
        runTask(task);
    }

    public void runTask(final Task task) {
        if (task == null) {
            return;
        }
        mSheduledThreadPoolExecutor.execute(task);
    }

    private static Task getTask(final Context context, final String authority, final Uri uri, final int code) {
        final Class klass = mCodeTaskClassMap.get(code);
        return createTask(context, authority, uri, klass);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected static final class EXTRAS {
        public static final String URI = "uri";
        public static final String FORCE_TASK = "forceTask";
    }

    public static <T extends Task> T createTask(final Context context, final String authority, final Uri uri, final Class<T> klass) {
        try {
            final Constructor constructor = klass.getDeclaredConstructor(Context.class, String.class, Uri.class);
            constructor.setAccessible(true);
            return (T) constructor.newInstance(context, authority, uri);
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

    public static void startTask(final Context context, final Uri uri, final Class klass) {
        final String uriString = uri.toString();
        final Intent intent = new Intent(context, klass);
        intent.putExtra(EXTRAS.URI, uriString);
        context.startService(intent);
    }

    public static void forceStartTask(final Context context, final Uri uri, final Class klass) {
        final String uriString = uri.toString();
        final Intent intent = new Intent(context, klass);
        intent.putExtra(EXTRAS.URI, uriString);
        intent.putExtra(EXTRAS.FORCE_TASK, true);

        context.startService(intent);
    }

    public static Set<Path> getPaths(final Class klass) {
        final Class<? extends Task> columnClass = klass;
        final Class<?>[] declaredClasses = columnClass.getDeclaredClasses();
        for (final Class<?> declaredClass : declaredClasses) {
            final Annotation[] declaredClassAnnotations = declaredClass.getDeclaredAnnotations();
            for (final Annotation declaredClassAnnotation : declaredClassAnnotations) {
                if (declaredClassAnnotation instanceof PathDefinitions) {
                    return getPathDefinitions(declaredClass);
                }
            }
        }
        return Sets.newHashSet();
    }

    public static Set<Path> getPathDefinitions(Class<?> declaredClass) {
        final Set<Path> paths = new HashSet<Path>();
        final Field[] declaredFields = declaredClass.getDeclaredFields();
        for (final Field declaredField : declaredFields) {
            final Annotation[] declaredFieldAnnotations = declaredField.getDeclaredAnnotations();
            for (final Annotation declaredFieldAnnotation : declaredFieldAnnotations) {
                if (declaredFieldAnnotation instanceof PathDefinition) {
                    try {
                        final Path path = (Path) declaredField.get(null);
                        paths.add(path);
                    } catch (IllegalAccessException e) {
                    }
                }
            }
        }
        return paths;
    }

}
