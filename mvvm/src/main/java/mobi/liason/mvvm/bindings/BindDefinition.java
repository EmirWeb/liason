package mobi.liason.mvvm.bindings;

import android.database.Cursor;
import android.net.Uri;

import mobi.liason.mvvm.utilities.IdCreator;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public abstract class BindDefinition {
    public void onUnBind(){}

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
     *
     * We recommend the following implementation.
     *
     * public static final int ID = IdCreator.getStaticId();
     *
     * @Override
     * public int getId(){
     *     return ID;
     * }
     *
     * @return Unique Id for the particular data flow
     */
    public abstract int getId();

}
