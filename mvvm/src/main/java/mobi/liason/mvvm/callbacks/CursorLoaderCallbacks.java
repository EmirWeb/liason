package mobi.liason.mvvm.callbacks;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mobi.liason.mvvm.bindings.BindDefinition;
import mobi.liason.mvvm.loaders.ForceLoadCursorLoader;

/**
 * Created by Emir Hasanbegovic on 18/04/14.
 */
public class CursorLoaderCallbacks implements LoaderCallbacks<Cursor> {

    private final Map<Integer, BindDefinition> mIdToBindDefinitionMap = new HashMap<Integer, BindDefinition>();
    private final Map<ForceLoadCursorLoader, BindDefinition> mLoaderToBindDefinitionMap = new HashMap<ForceLoadCursorLoader, BindDefinition>();
    private final Context mContext;
    private boolean mHasStarted;

    private LoaderManager mLoaderManager;

    public CursorLoaderCallbacks(final Context context, final LoaderManager loaderManager) {
        this(context, loaderManager, new ArrayList<BindDefinition>());
    }

    public CursorLoaderCallbacks(final Context context, final LoaderManager loaderManager, final BindDefinition bindDefinition) {
        this(context, loaderManager, Lists.newArrayList(bindDefinition));
    }

    public CursorLoaderCallbacks(final Context context, final LoaderManager loaderManager, final List<BindDefinition> bindDefinitions) {
        mLoaderManager = loaderManager;
        for (final BindDefinition bindDefinition : bindDefinitions) {
            addBindDefinition(bindDefinition);
        }

        mContext = context;
    }

    public void addBindDefinition(final BindDefinition bindDefinition){
        final int id = bindDefinition.getId();
        mIdToBindDefinitionMap.put(id, bindDefinition);
        if (mHasStarted){
            startLoader(id);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        final BindDefinition bindDefinition = mIdToBindDefinitionMap.get(id);
        final Uri uri = bindDefinition.getUri();
        final String[] projection = bindDefinition.getProjection();
        final String selection = bindDefinition.getSelection();
        final String[] selectionArguments = bindDefinition.getSelectionArguments();
        final String sortOrder = bindDefinition.getSortOrder();
        final ForceLoadCursorLoader forceLoadCursorLoader = new ForceLoadCursorLoader(mContext, uri, projection, selection, selectionArguments, sortOrder);
        forceLoadCursorLoader.setUri(uri);
        final ContentObserver contentObserver = forceLoadCursorLoader.getForceLoadContentObserver();
        mContext.getContentResolver().registerContentObserver(uri, false, contentObserver);
        mLoaderToBindDefinitionMap.put(forceLoadCursorLoader, bindDefinition);
        return forceLoadCursorLoader;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
        cursor.moveToFirst();
        final BindDefinition bindDefinition = mLoaderToBindDefinitionMap.get(loader);
        bindDefinition.onBind(cursor);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        final BindDefinition bindDefinition = mLoaderToBindDefinitionMap.get(loader);
        bindDefinition.onUnBind();
    }

    public void onStart(final Context context) {
        mHasStarted = true;
        final Set<Integer> ids = mIdToBindDefinitionMap.keySet();
        for (final Integer id : ids) {
            startLoader(id);
        }
    }

    private Loader<Cursor> startLoader(final int id) {
        final Loader<Cursor> loader = mLoaderManager.getLoader(id);
        if (loader == null) {
            return mLoaderManager.initLoader(id, null, this);
        }

        return mLoaderManager.restartLoader(id, null, this);
    }

    public void onStop(final Context context) {
        mHasStarted = false;
        final Set<ForceLoadCursorLoader> forceLoadCursorLoaders = mLoaderToBindDefinitionMap.keySet();
        for (final ForceLoadCursorLoader forceLoadCursorLoader : forceLoadCursorLoaders) {
            final ContentObserver contentObserver = forceLoadCursorLoader.getForceLoadContentObserver();
            context.getContentResolver().unregisterContentObserver(contentObserver);
        }
    }

}