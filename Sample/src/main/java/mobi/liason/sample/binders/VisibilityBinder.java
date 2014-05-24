package mobi.liason.sample.binders;

import android.content.Context;
import android.view.View;

import com.google.common.collect.Sets;

import java.util.Set;

import mobi.liason.mvvm.bindings.Binder;
import mobi.liason.mvvm.database.ViewModelColumn;

/**
 * Created by Emir Hasanbegovic on 2014-05-23.
 */
public class VisibilityBinder extends Binder {

    public VisibilityBinder(Set<Integer> resourceIds, ViewModelColumn viewModelColumn) {
        super(resourceIds, Sets.newHashSet(viewModelColumn));
    }

    public VisibilityBinder(int resourceId, ViewModelColumn viewModelColumn) {
        super(resourceId, viewModelColumn);
    }

    @Override
    public void onBind(Context context, View view, int resourceId, ViewModelColumn viewModelColumn, Object value) {
        final boolean isVisible = isVisible(viewModelColumn, value);
        if (isVisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public boolean isVisible(final ViewModelColumn viewModelColumn, final Object value) {
        final String isVisibleString = (String) value;
        return Boolean.parseBoolean(isVisibleString);
    }
}
