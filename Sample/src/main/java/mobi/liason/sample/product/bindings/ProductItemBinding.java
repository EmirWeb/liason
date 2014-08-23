package mobi.liason.sample.product.bindings;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import mobi.liason.mvvm.bindings.ActivityItemBinding;
import mobi.liason.mvvm.bindings.TextBinder;
import mobi.liason.mvvm.bindings.interfaces.Binding;
import mobi.liason.sample.R;
import mobi.liason.sample.binders.ImageBinder;
import mobi.liason.sample.overrides.SampleUriUtilities;
import mobi.liason.sample.product.viewmodels.ProductViewModel;

/**
 * Created by Emir Hasanbegovic on 24/05/14.
 */
public class ProductItemBinding extends ActivityItemBinding {
    private final long mId;

    public ProductItemBinding(final Activity activity, final long id) {
        super(activity);
        mId = id;

        final Binding nameTextBinding = new TextBinder(R.id.activity_product_name, ProductViewModel.Columns.NAME);
        addBinding(nameTextBinding);

        final Binding descriptionTextBinding = new TextBinder(R.id.activity_product_tasting_note, ProductViewModel.Columns.TASTING_NOTE);
        addBinding(descriptionTextBinding);

        final Binding imageBinding = new ImageBinder(R.id.activity_product_image, ProductViewModel.Columns.IMAGE_URL);
        addBinding(imageBinding);
    }

    @Override
    public Uri getUri(final Context context) {
        return SampleUriUtilities.getUri(context, ProductViewModel.Paths.PRODUCT_VIEW_MODEL, mId);
    }
}
