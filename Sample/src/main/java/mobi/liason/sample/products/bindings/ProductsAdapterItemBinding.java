package mobi.liason.sample.products.bindings;

import mobi.liason.mvvm.bindings.TextBinder;
import mobi.liason.mvvm.bindings.adapters.AdapterItemBinding;
import mobi.liason.mvvm.bindings.interfaces.Binding;
import mobi.liason.sample.R;
import mobi.liason.sample.products.viewmodels.ProductsViewModel;

/**
 * Created by Emir Hasanbegovic on 2014-05-27.
 */
public class ProductsAdapterItemBinding extends AdapterItemBinding {
    public ProductsAdapterItemBinding(int layoutResourceId) {
        super(layoutResourceId);

        final Binding textBinding = new TextBinder(R.id.list_item_product_name, ProductsViewModel.Columns.NAME);
        addBinding(textBinding);
    }
}
