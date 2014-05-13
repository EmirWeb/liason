package mobi.liason.mvvm.bindings;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;


/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class TextBinder extends Binder {

    private final boolean mIsGone;

    public TextBinder(final int resourceId, final String columnName) {
        this(resourceId, columnName, false);
    }

    public TextBinder(final int resourceId, final String columnName, final boolean isGone) {
        super(resourceId, columnName);
        mIsGone = isGone;
    }

    @Override
    public void onBind(Context context, Cursor cursor, final View view, int resourceId, int columnIndex, String columnName) {
        final String string = cursor.getString(columnIndex);
        final boolean isVisible = string != null;
        if (isVisible) {
            final TextView textView = (TextView) view;
            textView.setText(string);
            textView.setVisibility(View.VISIBLE);
        } else if (mIsGone) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }
}
