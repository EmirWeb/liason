package mobi.liason.mvvm;

import android.app.LoaderManager;
import android.content.Context;

import java.util.HashSet;
import java.util.Set;

import mobi.liason.mvvm.bindings.BindDefinition;
import mobi.liason.mvvm.callbacks.RestLoaderCallbacks;

/**
 * Created by Emir Hasanbegovic on 15/05/14.
 */
public class BindingManager {
    private final Set<RestLoaderCallbacks> callbacks = new HashSet<RestLoaderCallbacks>();
    private final LoaderManager mLoaderManager;
    private final Context mContext;
    private boolean hasStarted = false;

    public BindingManager (final Context context, final LoaderManager loaderManager){
        mContext = context;
        mLoaderManager = loaderManager;
    }

    public void addBindDefinition(final BindDefinition bindDefinition){
        final RestLoaderCallbacks restLoaderCallbacks = new RestLoaderCallbacks(mContext, mLoaderManager, bindDefinition);
        if (hasStarted){
            restLoaderCallbacks.onStart(mContext);
        }
    }

    public void onStart() {
        hasStarted = true;
        for (final RestLoaderCallbacks restLoaderCallbacks : callbacks){
            restLoaderCallbacks.onStart(mContext);
        }
    }

    public void onStop() {
        hasStarted = false;
        for (final RestLoaderCallbacks restLoaderCallbacks : callbacks){
            restLoaderCallbacks.onStop(mContext);
        }
    }
}
