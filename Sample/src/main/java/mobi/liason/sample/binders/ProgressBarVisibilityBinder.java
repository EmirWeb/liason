package mobi.liason.sample.binders;

import java.util.Set;

import mobi.liason.mvvm.database.ViewModelColumn;

/**
 * Created by Emir Hasanbegovic on 2014-05-23.
 */
public class ProgressBarVisibilityBinder extends VisibilityBinder {

    public ProgressBarVisibilityBinder(Set<Integer> resourceIds, ViewModelColumn viewModelColumn) {
        super(resourceIds, viewModelColumn);
    }

    public ProgressBarVisibilityBinder(int resourceId, ViewModelColumn viewModelColumn) {
        super(resourceId, viewModelColumn);
    }

    public boolean isVisible(final ViewModelColumn viewModelColumn, final Object value) {
        final String isVisibleString = (String) value;

        if (value == null) {
            return true;
        }

        return Boolean.parseBoolean(isVisibleString);
    }
}
