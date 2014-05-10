package mobi.liason.mvvm.callbacks;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.content.Loader.ForceLoadContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import mobi.liason.mvvm.bindings.Binding;
import mobi.liason.mvvm.loaders.RestCursorLoader;
import mobi.liason.mvvm.utilities.IdCreator;

/**
 * Created by Emir Hasanbegovic on 18/04/14.
 */
public abstract class RestLoaderCallbacks implements LoaderCallbacks<Cursor> {

    private static final IdCreator ID_CREATOR = new IdCreator();

    private final Binding mBinding;
    private final Context mContext;
    private final int mId;

    private LoaderManager mLoaderManager;
    private ForceLoadContentObserver mForceLoadContentObserver;

    public RestLoaderCallbacks(final Context context, final LoaderManager loaderManager, final Binding binding) {
        mLoaderManager = loaderManager;
        mBinding = binding;
        mContext = context;
        mId = ID_CREATOR.getId();
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        final Uri uri = mBinding.getUri();
        final String[] projection = mBinding.getProjection();
        final String selection = mBinding.getSelection();
        final String[] selectionArguments = mBinding.getSelectionArguments();
        final String sortOrder = mBinding.getSortOrder();
        final RestCursorLoader restCursorLoader = new RestCursorLoader(mContext, uri, projection, selection, selectionArguments, sortOrder);
        restCursorLoader.setUri(uri);
        mForceLoadContentObserver = (ForceLoadContentObserver) restCursorLoader.getForceLoadContentObserver();
        mContext.getContentResolver().registerContentObserver(uri, false, mForceLoadContentObserver);
        return restCursorLoader;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
        cursor.moveToFirst();
        mBinding.onBind(cursor);
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

    public void onDestroy(final Context context){

    }

}