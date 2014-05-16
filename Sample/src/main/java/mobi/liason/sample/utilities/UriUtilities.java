package mobi.liason.sample.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import mobi.liason.sample.R;

/**
 * Created by Emir Hasanbegovic on 15/05/14.
 */
public class UriUtilities {

    private static final String URI = "content://%s/%s";

    public static Uri getUri(final Context context, final String path){
        final Resources resources = context.getResources();
        final String authority = resources.getString(R.string.authority);
        final String uri = String.format(URI, authority, path);
        return Uri.parse(uri);
    }
}
