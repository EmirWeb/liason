package mobi.liason.mvvm.bindings.interfaces;

import android.content.Context;

/**
 * Created by Emir Hasanbegovic on 2014-05-10.
 */
public interface Binding {
    public void onBindStart(final Context context);
    public void onBindEnd(final Context context);
}
