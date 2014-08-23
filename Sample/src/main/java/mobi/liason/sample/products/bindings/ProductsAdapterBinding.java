package mobi.liason.sample.products.bindings;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import mobi.liason.mvvm.bindings.adapters.ActivityAdapterBinding;
import mobi.liason.mvvm.bindings.adapters.AdapterItemBinding;
import mobi.liason.sample.R;
import mobi.liason.sample.overrides.SampleUriUtilities;
import mobi.liason.sample.products.viewmodels.ProductsViewModel;

/**
 * Created by Emir Hasanbegovic on 15/05/14.
 */
public class ProductsAdapterBinding extends ActivityAdapterBinding {

    public ProductsAdapterBinding(final Activity activity, final int resourceId) {
        super(activity, resourceId);

        final AdapterItemBinding adapterItemBinding = new ProductsAdapterItemBinding(R.layout.list_item_product);
        addItemBinding(adapterItemBinding);

    }

    @Override
    public Uri getUri(final Context context) {
        return SampleUriUtilities.getUri(context, ProductsViewModel.Paths.PRODUCTS_VIEW_MODEL);
    }

}
