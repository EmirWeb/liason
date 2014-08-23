package mobi.liason.sample.products.tasks;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import java.util.ArrayList;

import mobi.liason.loaders.Path;
import mobi.liason.loaders.UriUtilities;
import mobi.liason.mvvm.task.Task;
import mobi.liason.sample.R;
import mobi.liason.sample.models.Product;
import mobi.liason.sample.models.ProductModel;
import mobi.liason.sample.overrides.SampleProvider;
import mobi.liason.sample.product.tasks.ProductResponse;
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

        final Uri networkUri = UriUtilities.getUri(SCHEME, AUTHORITY, Paths.PRODUCTS);
        final String url = networkUri.toString();
        final ProductsResponse productsResponse = TaskUtilities.getModel(url, ProductsResponse.class);

        final ArrayList<ContentProviderOperation> contentProviderOperations = getContentProviders(context, productsResponse);

        final String authority = SampleProvider.getProviderAuthority(context);

        final Uri uri = getUri();
        final ContentResolver contentResolver = context.getContentResolver();
        contentResolver.applyBatch(authority, contentProviderOperations);
        contentResolver.notifyChange(uri, null, false);

        final Uri productsViewModelUri = SampleProvider.getUri(context, ProductsViewModel.Paths.PRODUCTS_VIEW_MODEL);
        contentResolver.notifyChange(productsViewModelUri, null, false);
    }

    private ArrayList<ContentProviderOperation> getContentProviders(final Context context, final ProductsResponse productsResponse) {
        final ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<ContentProviderOperation>();
        final Uri tableUri = SampleProvider.getUri(context, ProductModel.Paths.PRODUCT_TABLE);
        final ContentProviderOperation deleteContentProviderOperation = ContentProviderOperation.newDelete(tableUri).build();
        contentProviderOperations.add(deleteContentProviderOperation);

        final ArrayList<Product> products = productsResponse.getProducts();
        for (final Product product : products) {
            final ContentValues contentValues = ProductModel.getContentValues(product);
            final ContentProviderOperation insertContentProviderOperation = ContentProviderOperation.newInsert(tableUri).withValues(contentValues).build();
            contentProviderOperations.add(insertContentProviderOperation);
        }
        return contentProviderOperations;
    }

    public static class Paths {
        public static final Path PRODUCTS = new Path(ProductsTask.PRODUCTS);
    }
}
