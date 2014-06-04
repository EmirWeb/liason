package mobi.liason.sample;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import mobi.liason.loaders.ActivityBindingManager;
import mobi.liason.sample.overrides.SampleTaskService;
import mobi.liason.sample.overrides.SampleUriUtilities;
import mobi.liason.sample.product.viewmodels.ProductViewModel;
import mobi.liason.sample.products.bindings.ProductsAdapterBinding;
import mobi.liason.sample.products.bindings.ProductsTaskStateBinding;
import mobi.liason.sample.products.tasks.ProductsTask;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class ProductsActivity extends Activity implements AdapterView.OnItemClickListener, OnRefreshListener {

    private ActivityBindingManager mActivityBindingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        mActivityBindingManager = new ActivityBindingManager(this);
        final AdapterView adapterView = (AdapterView) findViewById(R.id.activity_products_adapter_view);
        adapterView.setOnItemClickListener(this);

        final ProductsAdapterBinding productsAdapterBinding = new ProductsAdapterBinding(this, R.id.activity_products_adapter_view);
        mActivityBindingManager.addBindDefinition(productsAdapterBinding);

        final ProductsTaskStateBinding productsTaskStateBinding = new ProductsTaskStateBinding(this);
        mActivityBindingManager.addBindDefinition(productsTaskStateBinding);

        final PullToRefreshLayout pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.activity_products_pull_to_refresh_layout);
        ActionBarPullToRefresh.from(this).allChildrenArePullable().listener(this).setup(pullToRefreshLayout);

        getWindow().setBackgroundDrawable(null);

    }

    @Override
    protected void onStart() {
        super.onStart();
        final Context context = getApplicationContext();
        mActivityBindingManager.onStart(context);
    }

    @Override
    protected void onStop() {
        super.onStop();
        final Context context = getApplicationContext();
        mActivityBindingManager.onStop(context);
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
        final Uri uri = SampleUriUtilities.getUri(context, ProductsTask.Paths.PRODUCTS);
        SampleTaskService.forceStartTask(this, uri);
    }
}

