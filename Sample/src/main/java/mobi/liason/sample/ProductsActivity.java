package mobi.liason.sample;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import mobi.liason.sample.overrides.SampleProvider;
import mobi.liason.sample.overrides.SampleTaskService;
import mobi.liason.sample.product.viewmodels.ProductViewModel;
import mobi.liason.sample.products.bindings.ProductsAdapterBinding;
import mobi.liason.sample.products.bindings.ProductsTaskStateBinding;
import mobi.liason.sample.products.tasks.ProductsTask;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class ProductsActivity extends SampleActivity implements AdapterView.OnItemClickListener, OnRefreshListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        final AdapterView adapterView = (AdapterView) findViewById(R.id.activity_products_adapter_view);
        adapterView.setOnItemClickListener(this);

        setBindingDefinitions();

        final PullToRefreshLayout pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.activity_products_pull_to_refresh_layout);
        ActionBarPullToRefresh.from(this).allChildrenArePullable().listener(this).setup(pullToRefreshLayout);

        getWindow().setBackgroundDrawable(null);

    }

    private void setBindingDefinitions() {
        final ProductsAdapterBinding productsAdapterBinding = new ProductsAdapterBinding(this);
        addBindDefinition(productsAdapterBinding);

        final ProductsTaskStateBinding productsTaskStateBinding = new ProductsTaskStateBinding(this);
        addBindDefinition(productsTaskStateBinding);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        final long productId = (Long) ProductViewModel.Columns._ID.getValue(cursor);
        ProductActivity.startActivity(this, productId);
    }

    @Override
    public void onRefreshStarted(View view) {
        final Context context = getApplicationContext();
        final Uri uri = SampleProvider.getUri(context, ProductsTask.Paths.PRODUCTS);
        SampleTaskService.forceStartTask(this, uri);
    }
}

