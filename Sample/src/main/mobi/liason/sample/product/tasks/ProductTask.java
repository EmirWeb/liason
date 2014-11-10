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
import mobi.liason.mvvm.database.annotations.PathDefinition;
import mobi.liason.mvvm.database.annotations.PathDefinitions;
import mobi.liason.mvvm.task.Task;
import mobi.liason.sample.R;
import mobi.liason.sample.models.Product;
import mobi.liason.sample.product.tasks.ProductResponseJson;
import mobi.liason.sample.models.ProductJson;
import mobi.liason.sample.models.ProductModel;
import mobi.liason.sample.overrides.SampleProvider;
import mobi.liason.sample.product.viewmodels.ProductViewModel;
import mobi.liason.sample.products.viewmodels.ProductsTaskStateViewModel;
import mobi.liason.sample.utilities.TaskUtilities;

/**
 * Created by Emir Hasanbegovic on 2014-05-20.
 */
public class ProductTask extends Task {

    private static final String SCHEME = "HTTP";
    private static final String AUTHORITY = "lcboapi.com";
    public static final String PRODUCTS = "products";
    private final long mId;

    public ProductTask(final Context context, final String authorty, final Uri uri) {
        super(context, authorty, uri);
        final String idString = uri.getLastPathSegment();
        mId = Long.parseLong(idString);
    }

    @Override
    protected void onExecuteTask(final Context context) throws Exception {
        final Uri networkUri = UriUtilities.getUri(SCHEME, AUTHORITY, Paths.PRODUCT, mId);
        final String url = networkUri.toString();
        final ProductResponseJson productResponse = TaskUtilities.getModel(url, ProductResponseJson.class);

        final ArrayList<ContentProviderOperation> contentProviderOperations = getContentProviderOperations(context, productResponse);

        final String authority = SampleProvider.getProviderAuthority(context);

        final ContentResolver contentResolver = context.getContentResolver();
        contentResolver.applyBatch(authority, contentProviderOperations);

        final Uri productsViewModelUri = SampleProvider.getUri(context, ProductViewModel.Paths.PRODUCT_VIEW_MODEL, mId);
        contentResolver.notifyChange(productsViewModelUri, null, false);

        final Uri productsTaskStateViewModelUri = SampleProvider.getUri(context, ProductsTaskStateViewModel.Paths.PRODUCTS_TASK_STATE);
        contentResolver.notifyChange(productsTaskStateViewModelUri, null, false);
    }

    private ArrayList<ContentProviderOperation> getContentProviderOperations(final Context context, final ProductResponseJson productResponse) {
        final ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<ContentProviderOperation>();

        final Uri tableUri = SampleProvider.getUri(context, ProductModel.Paths.PRODUCT);
        final ProductJson product = productResponse.getResult();
        final ContentValues contentValues = ProductModel.getContentValues(product);
        final ContentProviderOperation insertContentProviderOperation = ContentProviderOperation.newInsert(tableUri).withValues(contentValues).build();
        contentProviderOperations.add(insertContentProviderOperation);
        return contentProviderOperations;
    }

    @PathDefinitions
    public static class Paths {
        @PathDefinition
        public static final Path PRODUCT = new Path(PRODUCTS, "#");
    }
}
