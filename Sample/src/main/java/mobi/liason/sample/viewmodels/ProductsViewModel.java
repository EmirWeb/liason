package mobi.liason.sample.viewmodels;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

import mobi.liason.loaders.Path;
import mobi.liason.mvvm.database.Column;
import mobi.liason.mvvm.database.ViewModel;
import mobi.liason.mvvm.database.ViewModelColumn;
import mobi.liason.sample.models.ProductTable;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class ProductsViewModel extends ViewModel {

    public static final String VIEW_NAME = "ProductView";
    private static final String SELECTION = ProductTable.TABLE_NAME;

    @Override
    public String getName(final Context context) {
        return VIEW_NAME;
    }

    @Override
    public List<Column> getColumns(Context context) {
        return Arrays.asList(Columns.COLUMNS);
    }

    @Override
    protected String getSelection(Context context) {
        return SELECTION;
    }

    @Override
    public List<Path> getPaths(Context context) {
        return Lists.newArrayList(Paths.PRODUCTS_VIEW_MODEL, Paths.PRODUCT_VIEW_MODEL);
    }

    @Override
    public Cursor query(Context context, SQLiteDatabase sqLiteDatabase, Path path, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (path.equals(Paths.PRODUCT_VIEW_MODEL)) {
            final String lastPathSegment = uri.getLastPathSegment();
            final String overridenSelection = Columns._ID + "=?";
            final String[] overridenSelectionArguments = {lastPathSegment};
            return super.query(context, sqLiteDatabase, path, uri, projection, overridenSelection, overridenSelectionArguments, sortOrder);
        } else if (path.equals(Paths.PRODUCTS_VIEW_MODEL)) {
            return super.query(context, sqLiteDatabase, path, uri, projection, selection, selectionArgs, sortOrder);
        }
        return null;
    }

    public static class Columns {
        public static final ViewModelColumn _ID = new ViewModelColumn(VIEW_NAME, BaseColumns._ID, ProductTable.Columns.ID);
        public static final ViewModelColumn NAME = new ViewModelColumn(VIEW_NAME, ProductTable.Columns.NAME);
        public static final ViewModelColumn IMAGE_THUMB_URL = new ViewModelColumn(VIEW_NAME, ProductTable.Columns.IMAGE_THUMB_URL);
        public static final Column[] COLUMNS = new Column[]{_ID, NAME, IMAGE_THUMB_URL};
    }

    public static class Paths {
        public static final Path PRODUCTS_VIEW_MODEL = new Path(VIEW_NAME);
        public static final Path PRODUCT_VIEW_MODEL = new Path(VIEW_NAME, "#");
    }


}

