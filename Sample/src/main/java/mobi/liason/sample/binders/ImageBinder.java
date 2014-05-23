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
        super(resourceId,viewModelColumn.getName());
    }


    @Override
    public void onBind(Context context, Cursor cursor, View view, int resourceId, int columnIndex, String columnName) {
        final ImageView imageView =(ImageView) view;
        final String url = cursor.getString(columnIndex);
        imageView.setImageBitmap(null);
        Picasso.with(context).load(url).into(imageView);
    }
}
