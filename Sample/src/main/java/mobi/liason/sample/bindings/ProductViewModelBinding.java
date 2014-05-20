package mobi.liason.sample.bindings;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.AbsListView;

import mobi.liason.mvvm.bindings.TextBinder;
import mobi.liason.mvvm.bindings.adapters.AdapterBinding;
import mobi.liason.mvvm.bindings.adapters.ItemTypeBinding;
import mobi.liason.mvvm.bindings.interfaces.Binding;
import mobi.liason.mvvm.utilities.IdCreator;
import mobi.liason.sample.R;
import mobi.liason.sample.content.viewmodel.ProductViewModel;
import mobi.liason.sample.utilities.UriUtilities;

/**
 * Created by Emir Hasanbegovic on 15/05/14.
 */
public class ProductViewModelBinding extends AdapterBinding {

    private static final int ID = IdCreator.getStaticId();
    private final Context mContext;

    public ProductViewModelBinding(final Activity activity, final int resourceId){
        super(activity.getApplicationContext(), activity, resourceId);
        mContext = activity.getApplicationContext();
    }

    @Override
    public Uri getUri() {
        return UriUtilities.getUri(mContext, ProductViewModel.Paths.PRODUCT_VIEW_MODEL);
    }

    @Override
    public int getId() {
        return ID;
    }
}
