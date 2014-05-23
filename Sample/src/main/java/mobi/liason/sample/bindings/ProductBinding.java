package mobi.liason.sample.bindings;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import mobi.liason.mvvm.bindings.TextBinder;
import mobi.liason.mvvm.bindings.adapters.AdapterBinding;
import mobi.liason.mvvm.bindings.adapters.ItemTypeBinding;
import mobi.liason.mvvm.bindings.interfaces.Binding;
import mobi.liason.mvvm.utilities.IdCreator;
import mobi.liason.sample.R;
import mobi.liason.sample.viewmodels.ProductViewModel;
import mobi.liason.sample.overrides.SampleUriUtilities;

/**
 * Created by Emir Hasanbegovic on 15/05/14.
 */
public class ProductBinding extends AdapterBinding {

    private static final int ID = IdCreator.getStaticId();
    private final Context mContext;

    public ProductBinding(final Activity activity, final int resourceId){
        super(activity.getApplicationContext(), activity, resourceId);
        mContext = activity.getApplicationContext();

        final ItemTypeBinding itemTypeBinding = new ItemTypeBinding(R.layout.list_item_activity_product_list_view);
        final Binding textBinding = new TextBinder(R.id.list_item_activity_product_list_view_product_name, ProductViewModel.Columns.NAME);
        final Binding imageBinding = new ImageBinder(R.id.list_item_activity_product_list_view_product_image, ProductViewModel.Columns.IMAGE_THUMB_URL);
        itemTypeBinding.addBinding(textBinding);
        itemTypeBinding.addBinding(imageBinding);
        addItemBinding(itemTypeBinding);
    }

    @Override
    public Uri getUri() {
        return SampleUriUtilities.getUri(mContext, ProductViewModel.Paths.PRODUCT_VIEW_MODEL);
    }

    @Override
    public int getId() {
        return ID;
    }
}
