package mobi.liason.sample.products.tasks;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import java.util.ArrayList;

import mobi.liason.loaders.Path;
import mobi.liason.mvvm.task.Task;
import mobi.liason.sample.R;
import mobi.liason.sample.models.Product;
import mobi.liason.sample.models.ProductTable;
import mobi.liason.sample.overrides.SampleUriUtilities;
import mobi.liason.sample.products.viewmodels.ProductsViewModel;
import mobi.liason.sample.utilities.TaskUtilities;

/**
 * Created by Emir Hasanbegovic on 2014-05-20.
 */
public class ProductsTask extends Task {

    private static final String SCHEME = "HTTP";
    private static final String AUTHORITY = "lcboapi.com";
    public static final String PRODUCTS = "products";

    public ProductsTask(final Context context, final String authorty, final Uri uri) {
        super(context, authorty, uri);
    }

    @Override
    protected void onExecuteTask(final Context context) throws Exception {

        final Uri networkUri = SampleUriUtilities.getUri(SCHEME, AUTHORITY, Paths.PRODUCTS);
        final String url = networkUri.toString();
        final ProductsResponse productsResponse = TaskUtilities.getModel(url, ProductsResponse.class);

        final Uri tableUri = SampleUriUtilities.getUri(context, ProductTable.Paths.PRODUCT_TABLE);
        final ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<ContentProviderOperation>();
        final ContentProviderOperation deleteContentProviderOperation = ContentProviderOperation.newDelete(tableUri).build();
        contentProviderOperations.add(deleteContentProviderOperation);

        final ArrayList<Product> products = productsResponse.getProducts();
        for (final Product product : products) {
            final ContentValues contentValues = ProductTable.getContentValues(product);
            final ContentProviderOperation insertContentProviderOperation = ContentProviderOperation.newInsert(tableUri).withValues(contentValues).build();
            contentProviderOperations.add(insertContentProviderOperation);
        }

        final Resources resources = context.getResources();
        final String authority = resources.getString(R.string.authority);

        final Uri uri = getUri();
        final ContentResolver contentResolver = context.getContentResolver();
        contentResolver.applyBatch(authority, contentProviderOperations);
        contentResolver.notifyChange(uri, null);

        final Uri productsViewModelUri = SampleUriUtilities.getUri(context, ProductsViewModel.Paths.PRODUCTS_VIEW_MODEL);
        contentResolver.notifyChange(productsViewModelUri, null);
    }

    public static class Paths {
        public static final Path PRODUCTS = new Path(ProductsTask.PRODUCTS);
    }
}
