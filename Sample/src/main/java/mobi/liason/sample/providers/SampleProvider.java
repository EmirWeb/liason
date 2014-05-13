package mobi.liason.sample.providers;

import android.content.Context;
import android.content.res.Resources;

import mobi.liason.mvvm.database.DatabaseHelper;
import mobi.liason.mvvm.providers.Provider;
import mobi.liason.sample.R;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class SampleProvider extends Provider {
    @Override
    public String getAuthority(Context context) {
        final Resources resources = context.getResources();
        final String authority = resources.getString(R.string.authority);
        return authority;
    }

    @Override
    protected DatabaseHelper onCreateDatabaseHelper(Context context) {
        return null;
    }
}
