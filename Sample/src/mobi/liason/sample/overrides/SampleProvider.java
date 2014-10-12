package mobi.liason.sample.overrides;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import mobi.liason.loaders.DatabaseHelper;
import mobi.liason.loaders.Path;
import mobi.liason.loaders.Provider;
import mobi.liason.loaders.UriUtilities;
import mobi.liason.sample.R;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class SampleProvider extends Provider {
    @Override
    public String getAuthority(final Context context) {
        return getProviderAuthority(context);
    }

    public static String getProviderAuthority(final Context context) {
        final Resources resources = context.getResources();
        final String authority = resources.getString(R.string.authority);
        return authority;
    }

    @Override
    protected DatabaseHelper onCreateDatabaseHelper(Context context) {
        return new SampleDatabaseHelper(context);
    }

    public static Uri getUri(final Context context, final Path path, final Object... objects) {
        final Resources resources = context.getResources();
        final String authority = getProviderAuthority(context);
        final String scheme = resources.getString(R.string.scheme);
        return UriUtilities.getUri(scheme, authority, path, objects);
    }
}
