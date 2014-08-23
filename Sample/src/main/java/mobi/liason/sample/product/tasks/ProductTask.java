package mobi.liason.sample.product.tasks;

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
import mobi.liason.sample.product.viewmodels.ProductViewModel;
import mobi.liason.sample.utilities.TaskUtilities;

/**
 * Created by Emir Hasanbegovic on 2014-05-20.
 */
public class ProductTask extends Task {

    private static final String SCHEME = "HTTP";
    private static final String AUTHORITY = "lcboapi.com";
    public static final String PRODUCTS = "products";

    public ProductTask(final Context context, final String authorty, final Uri uri) {
        super(context, authorty, uri);
    }

    @Override
    protected void onExecuteTask(final Context context) throws Exception {
        final Uri uri = getUri();
        final String idString = uri.getLastPathSegment();
        final long id = Long.parseLong(idString);
        final Uri networkUri = UriUtilities.getUri(SCHEME, AUTHORITY, Paths.PRODUCT, id);
        final String url = networkUri.toString();
        final ProductResponse productResponse = TaskUtilities.getModel(url, ProductResponse.class);
        final Uri tableUri = SampleProvider.getUri(context, ProductModel.Paths.PRODUCT_TABLE);

        final ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<ContentProviderOperation>();

        final Product product = productResponse.getProduct();
        final ContentValues contentValues = ProductModel.getContentValues(product);
        final ContentProviderOperation insertContentProviderOperation = ContentProviderOperation.newInsert(tableUri).withValues(contentValues).build();
        contentProviderOperations.add(insertContentProviderOperation);

        final String authority = SampleProvider.getProviderAuthority(context);

        final ContentResolver contentResolver = context.getContentResolver();
        contentResolver.applyBatch(authority, contentProviderOperations);
        contentResolver.notifyChange(uri, null, false);

        final Uri productsViewModelUri = SampleProvider.getUri(context, ProductViewModel.Paths.PRODUCT_VIEW_MODEL, id);
        contentResolver.notifyChange(productsViewModelUri, null, false);
    }

    public static class Paths {
        public static final Path PRODUCT = new Path(PRODUCTS, "#");
    }
}
