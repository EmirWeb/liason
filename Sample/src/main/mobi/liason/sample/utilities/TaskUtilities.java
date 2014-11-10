package mobi.liason.sample.utilities;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import mobi.liason.sample.product.tasks.ProductResponse;

/**
 * Created by Emir Hasanbegovic on 24/05/14.
 */
public class TaskUtilities {
    private static final Gson GSON = new Gson();

    public static <MODEL> MODEL getModel(final String url, final Class<MODEL> model) {
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
            return GSON.fromJson(streamToString(inputStreamReader), model);
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

    private static final String streamToString(final InputStreamReader inputStreamReader ){
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        int read;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((read = inputStreamReader.read(buffer, 0, bufferSize)) > 0) {
                stringBuilder.append(buffer, 0, read);
            }
        }catch (final Exception e){

        }
        final String string = stringBuilder.toString();
        Log.d("NetworkResponse", string);
        return string;
    }
}
