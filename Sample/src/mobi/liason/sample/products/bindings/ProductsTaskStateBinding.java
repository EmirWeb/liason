package mobi.liason.sample.products.bindings;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import mobi.liason.mvvm.bindings.ActivityItemBinding;
import mobi.liason.sample.R;
import mobi.liason.sample.binders.VisibilityBinder;
import mobi.liason.sample.overrides.SampleProvider;
import mobi.liason.sample.products.viewmodels.ProductsTaskStateViewModel;

/**
 * Created by Emir Hasanbegovic on 15/05/14.
 */
public class ProductsTaskStateBinding extends ActivityItemBinding {

    public ProductsTaskStateBinding(final Activity activity) {
        super(activity);

        final VisibilityBinder dataVisibilityBinder = new VisibilityBinder(R.id.activity_products_adapter_view, ProductsTaskStateViewModel.Columns.IS_DATA_VISIBLE);
        addBinding(dataVisibilityBinder);

    }

    @Override
    public Uri getUri(final Context context) {
        return SampleProvider.getUri(context, ProductsTaskStateViewModel.Paths.PRODUCTS_TASK_STATE);
    }

}
