package mobi.liason.sample.products.bindings;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import mobi.liason.mvvm.bindings.TextBinder;
import mobi.liason.mvvm.bindings.adapters.ActivityAdapterBinding;
import mobi.liason.mvvm.bindings.adapters.AdapterItemBinding;
import mobi.liason.mvvm.bindings.interfaces.Binding;
import mobi.liason.mvvm.utilities.IdCreator;
import mobi.liason.sample.R;
import mobi.liason.sample.binders.ImageBinder;
import mobi.liason.sample.overrides.SampleUriUtilities;
import mobi.liason.sample.products.viewmodels.ProductsViewModel;

/**
 * Created by Emir Hasanbegovic on 15/05/14.
 */
public class ProductsAdapterBinding extends ActivityAdapterBinding{

    private static final int ID = IdCreator.getStaticId();

    public ProductsAdapterBinding(final Activity activity, final int resourceId){
        super(activity, resourceId);

        final AdapterItemBinding adapterItemBinding = new AdapterItemBinding(R.layout.list_item_product);
        addItemBinding(adapterItemBinding);

        final Binding textBinding = new TextBinder(R.id.list_item_product_name, ProductsViewModel.Columns.NAME);
        adapterItemBinding.addBinding(textBinding);

        final Binding imageBinding = new ImageBinder(R.id.list_item_product_image, ProductsViewModel.Columns.IMAGE_THUMB_URL);
        adapterItemBinding.addBinding(imageBinding);
    }

    @Override
    public Uri getUri(final Context context) {
        return SampleUriUtilities.getUri(context, ProductsViewModel.Paths.PRODUCTS_VIEW_MODEL);
    }

    @Override
    public int getId(final Context context) {
        return ID;
    }
}
