package mobi.liason.sample.binders;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import mobi.liason.mvvm.bindings.Binder;
import mobi.liason.mvvm.database.ViewModelColumn;

/**
 * Created by Emir Hasanbegovic on 2014-05-23.
 */
public class ImageBinder extends Binder{

    public ImageBinder(int resourceId, ViewModelColumn viewModelColumn) {
        super(resourceId,viewModelColumn);
    }

    @Override
    public void onBind(Context context, View view, int resourceId, ViewModelColumn viewModelColumn, Object value) {
        final ImageView imageView =(ImageView) view;
        final String url = (String) value;
        imageView.setImageBitmap(null);
        Picasso.with(context).load(url).into(imageView);
    }
}
