package mobi.liason.sample;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import mobi.liason.mvvm.bindings.adapters.AdapterBinding;
import mobi.liason.mvvm.callbacks.CursorLoaderCallbacks;
import mobi.liason.sample.bindings.ProductViewModelBinding;
import mobi.liason.sample.content.models.Product;
import mobi.liason.sample.content.models.ProductTable;
import mobi.liason.sample.utilities.UriUtilities;

public class ProductActivity extends Activity {

    private CursorLoaderCallbacks mCursorLoaderCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        final Context context = getApplicationContext();
        final LoaderManager loaderManager = getLoaderManager();
        mCursorLoaderCallbacks = new CursorLoaderCallbacks(context, loaderManager);

        final AdapterBinding adapterBinding = new ProductViewModelBinding(context);
        mCursorLoaderCallbacks.addBindDefinition(adapterBinding);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Context context = getApplicationContext();
        mCursorLoaderCallbacks.onStart(context);
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
        contentResolver.bulkInsert(uri, contentValues);
        contentResolver.notifyChange(uri, null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        final Context context = getApplicationContext();
        mCursorLoaderCallbacks.onStop(context);
    }
}

