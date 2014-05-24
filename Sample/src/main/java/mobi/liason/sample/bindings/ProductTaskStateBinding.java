package mobi.liason.sample.bindings;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import java.util.HashSet;
import java.util.Set;

import mobi.liason.mvvm.bindings.ActivityItemBinding;
import mobi.liason.mvvm.utilities.IdCreator;
import mobi.liason.sample.R;
import mobi.liason.sample.binders.VisibilityBinder;
import mobi.liason.sample.overrides.SampleUriUtilities;
import mobi.liason.sample.viewmodels.ProductTaskStateViewModel;
import mobi.liason.sample.viewmodels.ProductsTaskStateViewModel;

/**
 * Created by Emir Hasanbegovic on 15/05/14.
 */
public class ProductTaskStateBinding extends ActivityItemBinding {

    private static final int ID = IdCreator.getStaticId();
    private final long mId;

    public ProductTaskStateBinding(final Activity activity, final long id) {
        super(activity);
        mId = id;

        final VisibilityBinder progressBarVisibilityBinder = new VisibilityBinder(R.id.activity_product_progress_bar, ProductsTaskStateViewModel.Columns.IS_PROGRESS_BAR_VISIBLE);
        addBinding(progressBarVisibilityBinder);

        final Set<Integer> dataResourceIds = new HashSet<Integer>();
        dataResourceIds.add(R.id.activity_product_image);
        dataResourceIds.add(R.id.activity_product_description);
        dataResourceIds.add(R.id.activity_product_name);
        dataResourceIds.add(R.id.activity_product_progress_bar);

        final VisibilityBinder dataVisibilityBinder = new VisibilityBinder(dataResourceIds, ProductsTaskStateViewModel.Columns.IS_DATA_VISIBLE);
        addBinding(dataVisibilityBinder);
    }

    @Override
    public Uri getUri(final Context context) {
        return SampleUriUtilities.getUri(context, ProductTaskStateViewModel.Paths.PRODUCTS_TASK_STATE, mId);
    }

    @Override
    public int getId(final Context context) {
        return ID;
    }

}
