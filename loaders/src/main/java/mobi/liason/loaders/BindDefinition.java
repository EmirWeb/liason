package mobi.liason.loaders;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public abstract class BindDefinition {

    private final Context mContext;

    public BindDefinition(final Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public void onUnBind() {
    }

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

    public abstract void onBind(final Cursor cursor);

    public abstract Uri getUri();

    /**
     * This is used to keep track of the loaders across Activity/Fragment life-cycles
     * <p/>
     * We recommend the following implementation.
     * <p/>
     * public static final int ID = IdCreator.getStaticId();
     *
     * @return Unique Id for the particular data flow
     * @Override public int getId(){
     * return ID;
     * }
     */
    public abstract int getId();

}
