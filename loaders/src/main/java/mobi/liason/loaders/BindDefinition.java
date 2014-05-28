package mobi.liason.loaders;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public abstract class BindDefinition {

    private final Context mContext;
    private BindingManager mBindingManager;

    public BindDefinition(final Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public void onUnBind(final Context context) {
    }

    public String[] getProjection(final Context context) {
        return null;
    }

    public String getSelection(final Context context) {
        return null;
    }

    public String[] getSelectionArguments(final Context context) {
        return null;
    }

    public String getSortOrder(final Context context) {
        return null;
    }

    public abstract void onBind(final Context context, final Cursor cursor);

    public abstract Uri getUri(final Context context);

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
    public abstract int getId(final Context context);

    public void rebind() {
        mBindingManager.restartLoader(this);
    }

    public void setManager(final BindingManager bindingManager) {
        mBindingManager = bindingManager;
    }
}
