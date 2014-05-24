package mobi.liason.sample;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import mobi.liason.loaders.BindingManager;
import mobi.liason.mvvm.bindings.adapters.AdapterBinding;
import mobi.liason.sample.bindings.ProductsAdapterBinding;
import mobi.liason.sample.bindings.ProductsTaskStateBinding;
import mobi.liason.sample.viewmodels.ProductsViewModel;

public class ProductsActivity extends Activity implements AdapterView.OnItemClickListener{

    private BindingManager mBindingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        final Context context = getApplicationContext();
        final LoaderManager loaderManager = getLoaderManager();
        mBindingManager = new BindingManager(context, loaderManager);
        final ListView listView = (ListView) findViewById(R.id.activity_products_list_view);
        listView.setOnItemClickListener(this);
        final AdapterBinding adapterBinding = new ProductsAdapterBinding(this, R.id.activity_products_list_view);
        final ProductsTaskStateBinding productsTaskStateBinding = new ProductsTaskStateBinding(this, R.id.activity_products_progress_bar, R.id.activity_products_list_view);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        final long productId = (Long) ProductsViewModel.Columns._ID.getValue(cursor);
        ProductActivity.startActivity(this, productId);
    }
}

