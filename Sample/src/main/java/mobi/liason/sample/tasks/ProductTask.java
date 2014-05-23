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
import java.util.List;

import mobi.liason.mvvm.providers.Path;
import mobi.liason.sample.R;
import mobi.liason.sample.content.models.Product;
import mobi.liason.sample.content.models.ProductTable;
import mobi.liason.sample.content.viewmodel.ProductViewModel;
import mobi.liason.sample.services.Task;
import mobi.liason.sample.utilities.UriUtilities;

/**
 * Created by Emir Hasanbegovic on 2014-05-20.
 */
public class ProductTask extends Task {

    private static final String SCHEME = "HTTP";
    private static final String AUTHORITY = "lcboapi.com";
    public static final Gson GSON = new Gson();

    public ProductTask(Context context, Uri uri) {
        super(context, uri);
    }

    @Override
    protected void onExecuteTask(final Context context) throws Exception{
        final Uri uri = UriUtilities.getUri(SCHEME, AUTHORITY, Paths.PRODUCTS);
        final String url = uri.toString();
        final ProductResponse productResponse = getProductResponse(url);

        final Uri tableUri = UriUtilities.getUri(context, ProductTable.Paths.PRODUCT_TABLE);

        final ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<ContentProviderOperation>();
        final ContentProviderOperation deleteContentProviderOperation = ContentProviderOperation.newDelete(tableUri).build();
        contentProviderOperations.add(deleteContentProviderOperation);

        final ArrayList<Product> products = productResponse.getProducts();
        for (final Product product : products){
            final ContentValues contentValues = ProductTable.getContentValues(product);
            final ContentProviderOperation insertContentProviderOperation = ContentProviderOperation.newInsert(tableUri).withValues(contentValues).build();
            contentProviderOperations.add(insertContentProviderOperation);
        }

        final Resources resources = context.getResources();
        final String authority = resources.getString(R.string.authority);

        final ContentResolver contentResolver = context.getContentResolver();
        contentResolver.applyBatch(authority, contentProviderOperations);

        final Uri modelViewUri = UriUtilities.getUri(context, ProductViewModel.Paths.PRODUCT_VIEW_MODEL);
        contentResolver.notifyChange(modelViewUri, null);
    }

    private ProductResponse getProductResponse(final String url){
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
            return  GSON.fromJson(inputStreamReader, ProductResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStreamReader != null){
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                }
            }
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public static class Paths {
        public static final Path PRODUCTS = new Path("products");
    }
}
