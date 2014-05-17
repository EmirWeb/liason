package mobi.liason.mvvm;

import android.app.LoaderManager;
import android.content.Context;

import java.util.HashSet;
import java.util.Set;

import mobi.liason.mvvm.bindings.BindDefinition;
import mobi.liason.mvvm.callbacks.CursorLoaderCallbacks;

/**
 * Created by Emir Hasanbegovic on 15/05/14.
 */
public class BindingManager {
    private final Set<CursorLoaderCallbacks> callbacks = new HashSet<CursorLoaderCallbacks>();
    private final LoaderManager mLoaderManager;
    private final Context mContext;
    private boolean hasStarted = false;

    public BindingManager (final Context context, final LoaderManager loaderManager){
        mContext = context;
        mLoaderManager = loaderManager;
    }

    public void addBindDefinition(final BindDefinition bindDefinition){
        final CursorLoaderCallbacks cursorLoaderCallbacks = new CursorLoaderCallbacks(mContext, mLoaderManager, bindDefinition);
        if (hasStarted){
            cursorLoaderCallbacks.onStart(mContext);
        }
    }

    public void onStart() {
        hasStarted = true;
        for (final CursorLoaderCallbacks cursorLoaderCallbacks : callbacks){
            cursorLoaderCallbacks.onStart(mContext);
        }
    }

    public void onStop() {
        hasStarted = false;
        for (final CursorLoaderCallbacks cursorLoaderCallbacks : callbacks){
            cursorLoaderCallbacks.onStop(mContext);
        }
    }
}
