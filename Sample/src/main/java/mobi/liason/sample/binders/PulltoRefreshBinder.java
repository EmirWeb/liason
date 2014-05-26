package mobi.liason.sample.binders;

import android.content.Context;
import android.view.View;

import java.util.Set;

import mobi.liason.mvvm.database.ViewModelColumn;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;

/**
 * Created by Emir Hasanbegovic on 2014-05-23.
 */
public class PulltoRefreshBinder extends VisibilityBinder {

    public PulltoRefreshBinder(Set<Integer> resourceIds, ViewModelColumn viewModelColumn) {
        super(resourceIds, viewModelColumn);
    }

    public PulltoRefreshBinder(int resourceId, ViewModelColumn viewModelColumn) {
        super(resourceId, viewModelColumn);
    }

    @Override
    public void onBind(Context context, View view, int resourceId, ViewModelColumn viewModelColumn, Object value) {
        final boolean isVisible = isVisible(viewModelColumn, value);
        final PullToRefreshLayout pullToRefreshLayout = (PullToRefreshLayout) view;

        pullToRefreshLayout.setRefreshing(isVisible);
    }

}
