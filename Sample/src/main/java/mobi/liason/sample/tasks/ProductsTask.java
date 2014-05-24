package mobi.liason.sample.tasks;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import mobi.liason.loaders.Path;
import mobi.liason.mvvm.network.Task;
import mobi.liason.sample.R;
import mobi.liason.sample.models.Product;
import mobi.liason.sample.models.ProductTable;
import mobi.liason.sample.overrides.SampleUriUtilities;
import mobi.liason.sample.viewmodels.ProductsViewModel;

/**
 * Created by Emir Hasanbegovic on 2014-05-20.
 */
public class ProductsTask extends Task {

    private static final String SCHEME = "HTTP";
    private static final String AUTHORITY = "lcboapi.com";
    public static final Gson GSON = new Gson();
    public static final String PRODUCTS = "products";

    public ProductsTask(final Context context, final String authorty, final Uri uri) {
        super(context, authorty, uri);
    }

    @Override
    protected void onExecuteTask(final Context context) throws Exception {
        final Uri uri = SampleUriUtilities.getUri(SCHEME, AUTHORITY, Paths.PRODUCTS);
        final String url = uri.toString();
        final ProductsResponse productsResponse = getProductResponse(url);

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

        final ContentResolver contentResolver = context.getContentResolver();
        contentResolver.applyBatch(authority, contentProviderOperations);

        final Uri modelViewUri = SampleUriUtilities.getUri(context, ProductsViewModel.Paths.PRODUCTS_VIEW_MODEL);
        contentResolver.notifyChange(modelViewUri, null);
    }

    private ProductsResponse getProductResponse(final String url) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        try {
            final HttpClient httpclient = new DefaultHttpClient();

            final HttpGet request = new HttpGet();
            final URI website = new URI(url);
            request.setURI(website);
            HttpResponse response = httpclient.execute(request);
            inputStream = response.getEntity().getContent();
            inputStreamReader = new InputStreamReader(inputStream);
            return GSON.fromJson(inputStreamReader, ProductsResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public static class Paths {
        public static final Path PRODUCTS = new Path(ProductsTask.PRODUCTS);
    }
}
