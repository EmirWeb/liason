package mobi.liason.mvvm.bindings;

import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public abstract class BindDefinition {

    public abstract void onBind(final Cursor cursor);

    public abstract Uri getUri();

    public String[] getProjection() {
        return null;
    }

    public String getSelection() {
        return null;
    }

    public String[] getSelectionArguments() {
        return null;
    }

    public String getSortOrder() {
        return null;
    }

}
