package mobi.liason.loaders.support;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mobi.liason.loaders.BindDefinition;
import mobi.liason.loaders.ForceLoadCursorLoader;

/**
 * Created by Emir Hasanbegovic on 18/04/14.
 */
public class SupportBindingManager implements LoaderCallbacks<Cursor> {

    private final Map<Integer, BindDefinition> mIdToBindDefinitionMap = new HashMap<Integer, BindDefinition>();
    private final Map<SupportForceLoadCursorLoader, BindDefinition> mLoaderToBindDefinitionMap = new HashMap<SupportForceLoadCursorLoader, BindDefinition>();
    private final Context mContext;
    private boolean mHasStarted;

    private LoaderManager mLoaderManager;

    public SupportBindingManager(final Context context, final LoaderManager loaderManager) {
        this(context, loaderManager, new ArrayList<BindDefinition>());
    }

    public SupportBindingManager(final Context context, final LoaderManager loaderManager, final BindDefinition bindDefinition) {
        this(context, loaderManager, Lists.newArrayList(bindDefinition));
    }

    public SupportBindingManager(final Context context, final LoaderManager loaderManager, final List<BindDefinition> bindDefinitions) {
        mLoaderManager = loaderManager;
        for (final BindDefinition bindDefinition : bindDefinitions) {
            addBindDefinition(bindDefinition);
        }

        mContext = context;
    }

    public void addBindDefinition(final BindDefinition bindDefinition) {
        final int id = bindDefinition.getId(mContext);
        mIdToBindDefinitionMap.put(id, bindDefinition);
        bindDefinition.setManager(this);
        if (mHasStarted) {
            startLoader(id);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        final BindDefinition bindDefinition = mIdToBindDefinitionMap.get(id);
        final Uri uri = bindDefinition.getUri(mContext);
        final String[] projection = bindDefinition.getProjection(mContext);
        final String selection = bindDefinition.getSelection(mContext);
        final String[] selectionArguments = bindDefinition.getSelectionArguments(mContext);
        final String sortOrder = bindDefinition.getSortOrder(mContext);
        final SupportForceLoadCursorLoader forceLoadCursorLoader = new SupportForceLoadCursorLoader(mContext, uri, projection, selection, selectionArguments, sortOrder);
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
        bindDefinition.onBind(mContext, cursor);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        final BindDefinition bindDefinition = mLoaderToBindDefinitionMap.get(loader);
        bindDefinition.onUnBind(mContext);
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
        final Set<SupportForceLoadCursorLoader> forceLoadCursorLoaders = mLoaderToBindDefinitionMap.keySet();
        for (final SupportForceLoadCursorLoader forceLoadCursorLoader : forceLoadCursorLoaders) {
            final ContentObserver contentObserver = forceLoadCursorLoader.getForceLoadContentObserver();
            context.getContentResolver().unregisterContentObserver(contentObserver);
        }
    }

    public void restartLoader(final BindDefinition bindDefinition) {
        for (final BindDefinition idMappedBindDefinition : mIdToBindDefinitionMap.values()) {
            if (bindDefinition == idMappedBindDefinition) {
                for (final SupportForceLoadCursorLoader forceLoadCursorLoader : mLoaderToBindDefinitionMap.keySet()) {
                    final BindDefinition loaderMappedBindDefinition = mLoaderToBindDefinitionMap.get(forceLoadCursorLoader);
                    if (loaderMappedBindDefinition == bindDefinition){
                        forceLoadCursorLoader.reset();
                        return;
                    }
                }
            }
        }
    }
}