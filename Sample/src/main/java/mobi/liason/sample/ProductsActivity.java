package mobi.liason.sample;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.os.Bundle;

import mobi.liason.loaders.BindingManager;
import mobi.liason.mvvm.bindings.adapters.AdapterBinding;
import mobi.liason.sample.bindings.ProductsAdapterBinding;
import mobi.liason.sample.bindings.ProductsTaskStateBinding;

public class ProductsActivity extends Activity {

    private BindingManager mBindingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        final Context context = getApplicationContext();
        final LoaderManager loaderManager = getLoaderManager();
        mBindingManager = new BindingManager(context, loaderManager);

        final AdapterBinding adapterBinding = new ProductsAdapterBinding(this, R.id.activity_product_list_view);


        final ProductsTaskStateBinding productsTaskStateBinding = new ProductsTaskStateBinding(this, R.id.activity_product_progress_bar, R.id.activity_product_list_view);

        mBindingManager.addBindDefinition(adapterBinding);
        mBindingManager.addBindDefinition(productsTaskStateBinding);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Context context = getApplicationContext();
        mBindingManager.onStart(context);
    }

    @Override
    protected void onStop() {
        super.onStop();
        final Context context = getApplicationContext();
        mBindingManager.onStop(context);
    }
}

