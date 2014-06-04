package mobi.liason.loaders;

import android.net.Uri;

import java.util.List;

import mobi.liason.loaders.Path;

/**
 * Created by Emir Hasanbegovic on 15/05/14.
 */
public class UriUtilities {


    public static Uri getUri(final String scheme, final String authority, final Path path, final Object... objects) {
        final Uri.Builder builder = new Uri.Builder().scheme(scheme).authority(authority);
        final List<String> pathSegments = path.getPathSegments(objects);
        for (final String pathSegment : pathSegments) {
            builder.appendPath(pathSegment);
        }

        return builder.build();
    }

}
