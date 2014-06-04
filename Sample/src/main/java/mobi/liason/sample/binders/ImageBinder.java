package mobi.liason.sample.binders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import mobi.liason.mvvm.bindings.Binder;
import mobi.liason.mvvm.database.ViewModelColumn;
import mobi.liason.sample.R;

/**
 * Created by Emir Hasanbegovic on 2014-05-23.
 */
public class ImageBinder extends Binder {

    public ImageBinder(final int resourceId, final ViewModelColumn viewModelColumn) {
        super(resourceId, viewModelColumn);
    }

    @Override
    public void onBind(final Context context, final View view, final int resourceId, final ViewModelColumn viewModelColumn, final Object value) {
        final ImageView imageView = (ImageView) view;
        final String url = (String) value;
        imageView.setImageBitmap(null);
        Picasso.with(context).load(url).placeholder(R.drawable.list_item_product_image_not_found).into(imageView);
    }
}
