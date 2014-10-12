package mobi.liason.sample.products.bindings;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import mobi.liason.mvvm.bindings.TextBinder;
import mobi.liason.mvvm.bindings.adapters.ActivityAdapterBinding;
import mobi.liason.mvvm.bindings.adapters.AdapterItemBinding;
import mobi.liason.mvvm.bindings.interfaces.Binding;
import mobi.liason.sample.R;
import mobi.liason.sample.binders.ImageBinder;
import mobi.liason.sample.overrides.SampleProvider;
import mobi.liason.sample.products.viewmodels.ProductsViewModel;

/**
 * Created by Emir Hasanbegovic on 15/05/14.
 */
public class ProductsAdapterBinding extends ActivityAdapterBinding {

    public ProductsAdapterBinding(final Activity activity) {
        super(activity, R.id.activity_products_adapter_view);

        final AdapterItemBinding adapterItemBinding = new AdapterItemBinding(R.layout.list_item_product);
        addItemBinding(adapterItemBinding);

        final Binding textBinding = new TextBinder(R.id.list_item_product_name, ProductsViewModel.Columns.NAME);
        final ImageBinder imageBinder = new ImageBinder(R.id.list_item_product_image, ProductsViewModel.Columns.IMAGE_THUMB_URL);
        adapterItemBinding.addBinding(textBinding);
        adapterItemBinding.addBinding(imageBinder);

    }

    @Override
    public Uri getUri(final Context context) {
        return SampleProvider.getUri(context, ProductsViewModel.Paths.PRODUCTS_VIEW_MODEL);
    }

}
