package mobi.liason.sample.bindings;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import mobi.liason.mvvm.bindings.ActivityItemBinding;
import mobi.liason.mvvm.utilities.IdCreator;
import mobi.liason.sample.R;
import mobi.liason.sample.binders.ProgressBarVisibilityBinder;
import mobi.liason.sample.binders.VisibilityBinder;
import mobi.liason.sample.overrides.SampleUriUtilities;
import mobi.liason.sample.viewmodels.ProductTaskStateViewModel;

/**
 * Created by Emir Hasanbegovic on 15/05/14.
 */
public class ProductTaskStateBinding extends ActivityItemBinding {

    private static final int ID = IdCreator.getStaticId();
    private final long mId;

    public ProductTaskStateBinding(final Activity activity, final long id) {
        super(activity);
        mId = id;

        final ProgressBarVisibilityBinder progressBarVisibilityBinder = new ProgressBarVisibilityBinder(R.id.activity_product_progress_bar, ProductTaskStateViewModel.Columns.IS_PROGRESS_BAR_VISIBLE);
        addBinding(progressBarVisibilityBinder);

        final Set<Integer> dataResourceIds = new HashSet<Integer>();
        dataResourceIds.add(R.id.activity_product_image);
        dataResourceIds.add(R.id.activity_product_description);
        dataResourceIds.add(R.id.activity_product_name);

        final VisibilityBinder dataVisibilityBinder = new VisibilityBinder(dataResourceIds, ProductTaskStateViewModel.Columns.IS_DATA_VISIBLE);
        addBinding(dataVisibilityBinder);
    }

    @Override
    public Uri getUri(final Context context) {
        final Uri uri = SampleUriUtilities.getUri(context, ProductTaskStateViewModel.Paths.PRODUCT_TASK_STATE, mId);
        Log.d("onBind", "getUri uri: " + uri);
        return  uri;
    }

    @Override
    public int getId(final Context context) {
        return ID;
    }

}
