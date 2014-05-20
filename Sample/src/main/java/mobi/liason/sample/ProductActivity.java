package mobi.liason.sample;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import mobi.liason.mvvm.bindings.TextBinder;
import mobi.liason.mvvm.bindings.adapters.AdapterBinding;
import mobi.liason.mvvm.bindings.adapters.ItemTypeBinding;
import mobi.liason.mvvm.bindings.interfaces.Binding;
import mobi.liason.mvvm.callbacks.BindingManager;
import mobi.liason.sample.bindings.ProductViewModelBinding;
import mobi.liason.sample.content.models.Product;
import mobi.liason.sample.content.models.ProductTable;
import mobi.liason.sample.content.viewmodel.ProductViewModel;
import mobi.liason.sample.utilities.UriUtilities;

public class ProductActivity extends Activity {

    private BindingManager mBindingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        final Context context = getApplicationContext();
        final LoaderManager loaderManager = getLoaderManager();
        mBindingManager = new BindingManager(context, loaderManager);

        final AdapterBinding adapterBinding = new ProductViewModelBinding(this, R.id.activity_product_list_view);
        final ItemTypeBinding itemTypeBinding = new ItemTypeBinding(R.layout.list_item_activity_product_list_view);
        final Binding binding = new TextBinder(R.id.list_item_activity_product_list_view_product_name, ProductViewModel.Columns.NAME);
        itemTypeBinding.addBinding(binding);

        adapterBinding.addItemBinding(itemTypeBinding);

        mBindingManager.addBindDefinition(adapterBinding);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Context context = getApplicationContext();
        mBindingManager.onStart(context);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Context context = getApplicationContext();
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri uri = UriUtilities.getUri(context, ProductTable.Paths.PRODUCT_TABLE);


        final ContentValues[] contentValues = new ContentValues[100];
        for (int index = 0; index < contentValues.length; index++) {
            final String string = "index: " + index;
            final Product product = new Product((long) index, string, null, null);
            contentValues[index] = ProductTable.getContentValues(product);
        }
        contentResolver.delete(uri, null, null);
        contentResolver.bulkInsert(uri, contentValues);
        contentResolver.notifyChange(uri, null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        final Context context = getApplicationContext();
        mBindingManager.onStop(context);
    }
}

