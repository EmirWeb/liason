package mobi.liason.sample.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import java.util.List;

import mobi.liason.mvvm.providers.Path;
import mobi.liason.sample.R;

/**
 * Created by Emir Hasanbegovic on 15/05/14.
 */
public class UriUtilities {

    public static Uri getUri(final Context context, final Path path, final Object... objects){
        final Resources resources = context.getResources();
        final String authority = resources.getString(R.string.authority);
        final String scheme = resources.getString(R.string.scheme);
        final Uri.Builder builder = new Uri.Builder().scheme(scheme).authority(authority);
        final List<String> pathSegments = path.getPathSegments(objects);
        for (final String pathSegment : pathSegments){
            builder.appendPath(pathSegment);
        }

        return builder.build();
    }
}
