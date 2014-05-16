package mobi.liason.mvvm.bindings;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import mobi.liason.mvvm.database.ViewModel;
import mobi.liason.mvvm.database.ViewModelColumn;


/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class TextBinder extends Binder {

    private final boolean mIsGone;
    private static final boolean DEFAULT_IS_GONE = false;

    public TextBinder(final int resourceId, final ViewModelColumn viewModelColumn) {
        this(resourceId, viewModelColumn.getName(), DEFAULT_IS_GONE);
    }

    public TextBinder(final int resourceId, final String columnName) {
        this(resourceId, columnName, DEFAULT_IS_GONE);
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
