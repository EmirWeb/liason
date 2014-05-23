package mobi.liason.sample.bindings;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;

import mobi.liason.loaders.BindDefinition;
import mobi.liason.mvvm.utilities.IdCreator;
import mobi.liason.sample.viewmodels.ProductTaskStateViewModel;
import mobi.liason.sample.overrides.SampleUriUtilities;

/**
 * Created by Emir Hasanbegovic on 15/05/14.
 */
public class ProductTaskStateBinding extends BindDefinition {

    private static final int ID = IdCreator.getStaticId();
    private final Context mContext;
    private final View mDataView;
    private final View mProgressBar;

    public ProductTaskStateBinding(final Activity activity, final int progressBarResourceId, final int dataResourceId){
        super(activity.getApplicationContext());
        mContext = activity.getApplicationContext();
        mProgressBar = activity.findViewById(progressBarResourceId);
        mDataView = activity.findViewById(dataResourceId);
    }

    @Override
    public void onBind(final Cursor cursor) {
        final boolean hasResults = cursor.moveToFirst();
        if (!hasResults){
            final int progressBarVisibility = getVisibility(true);
            mProgressBar.setVisibility(progressBarVisibility);

            final int dataVisibility = getVisibility(false);
            mDataView.setVisibility(dataVisibility);

            return;
        }
        
        final int progressBarVisibility = getVisibility(cursor, ProductTaskStateViewModel.Columns.IS_PROGRESS_BAR_VISIBLE.getName());
        mProgressBar.setVisibility(progressBarVisibility);

        final int dataVisibility = getVisibility(cursor, ProductTaskStateViewModel.Columns.IS_DATA_VISIBLE.getName());
        mDataView.setVisibility(dataVisibility);
    }

    private int getVisibility(final Cursor cursor, final String columnName) {
        final int columnIndex = cursor.getColumnIndex(columnName);
        final String string = cursor.getString(columnIndex);
        final boolean isVisible = string != null && Boolean.parseBoolean(string);
        return getVisibility(isVisible);
    }

    private int getVisibility(final boolean isVisible){
        if (isVisible) {
            return View.VISIBLE;
        }
        return View.GONE;
    }

    @Override
    public Uri getUri() {
        return SampleUriUtilities.getUri(mContext, ProductTaskStateViewModel.Paths.PRODUCT_TASK_STATE);
    }

    @Override
    public int getId() {
        return ID;
    }
}
