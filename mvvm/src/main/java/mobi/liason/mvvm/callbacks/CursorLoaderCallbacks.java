package mobi.liason.mvvm.callbacks;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.content.Loader.ForceLoadContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import mobi.liason.mvvm.bindings.BindDefinition;
import mobi.liason.mvvm.loaders.ForceLoadCursorLoader;
import mobi.liason.mvvm.loaders.ForceLoadCursorLoader;
import mobi.liason.mvvm.utilities.IdCreator;

/**
 * Created by Emir Hasanbegovic on 18/04/14.
 */
public class CursorLoaderCallbacks implements LoaderCallbacks<Cursor> {

    private static final IdCreator ID_CREATOR = new IdCreator();

    private final BindDefinition mBindDefinition;
    private final Context mContext;
    private final int mId;

    private LoaderManager mLoaderManager;
    private ForceLoadContentObserver mForceLoadContentObserver;

    public CursorLoaderCallbacks(final Context context, final LoaderManager loaderManager, final BindDefinition bindDefinition) {
        mLoaderManager = loaderManager;
        mBindDefinition = bindDefinition;
        mContext = context;
        mId = ID_CREATOR.getId();
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        final Uri uri = mBindDefinition.getUri();
        final String[] projection = mBindDefinition.getProjection();
        final String selection = mBindDefinition.getSelection();
        final String[] selectionArguments = mBindDefinition.getSelectionArguments();
        final String sortOrder = mBindDefinition.getSortOrder();
        final ForceLoadCursorLoader forceLoadCursorLoader = new ForceLoadCursorLoader(mContext, uri, projection, selection, selectionArguments, sortOrder);
        forceLoadCursorLoader.setUri(uri);
        mForceLoadContentObserver = (ForceLoadContentObserver) forceLoadCursorLoader.getForceLoadContentObserver();
        mContext.getContentResolver().registerContentObserver(uri, false, mForceLoadContentObserver);
        return forceLoadCursorLoader;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
        cursor.moveToFirst();
        mBindDefinition.onBind(cursor);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
    }

    public void onStart(final Context context) {
        final Loader<?> loader = mLoaderManager.getLoader(mId);
        if (loader == null)
            mLoaderManager.initLoader(mId, null, this);
        else
            mLoaderManager.restartLoader(mId, null, this);
    }

    public void onStop(final Context context) {
        if (mForceLoadContentObserver != null)
            mContext.getContentResolver().unregisterContentObserver(mForceLoadContentObserver);
    }
}