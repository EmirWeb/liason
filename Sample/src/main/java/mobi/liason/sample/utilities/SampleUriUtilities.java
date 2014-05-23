package mobi.liason.sample.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import mobi.liason.loaders.Path;
import mobi.liason.mvvm.utilities.UriUtilities;
import mobi.liason.sample.R;

/**
 * Created by Emir Hasanbegovic on 15/05/14.
 */
public class SampleUriUtilities extends UriUtilities {

    public static Uri getUri(final Context context, final Path path, final Object... objects) {
        final Resources resources = context.getResources();
        final String authority = resources.getString(R.string.authority);
        final String scheme = resources.getString(R.string.scheme);
        return getUri(scheme, authority, path, objects);
    }
}
