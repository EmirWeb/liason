package mobi.liason.loaders;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import mobi.liason.loaders.support.SupportBindingManager;
import mobi.liason.loaders.utilities.IdCreator;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public abstract class BindDefinition {

    private final Context mContext;
    private BindingManager mBindingManager;
    private SupportBindingManager mSupportBindingManager;
    private static final Map<Class, Integer> CLASS_ID_MAP = new HashMap<Class, Integer>();
    private static final IdCreator ID_CREATOR = new IdCreator();


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
     */
    public synchronized int getId(Context context) {
        final Class thisClass = this.getClass();
        if (CLASS_ID_MAP.containsKey(thisClass)){
            return CLASS_ID_MAP.get(thisClass);
        }

        final int id = ID_CREATOR.getId();
        CLASS_ID_MAP.put(thisClass, id);
        return id;
    }

    public void rebind() {
        if (mSupportBindingManager != null) {
            mSupportBindingManager.restartLoader(this);
        } else {
            mBindingManager.restartLoader(this);
        }
    }

    public void setManager(final BindingManager bindingManager) {
        mBindingManager = bindingManager;
    }

    public void setManager(final SupportBindingManager supportBindingManager) {
        mSupportBindingManager = supportBindingManager;
    }
}
