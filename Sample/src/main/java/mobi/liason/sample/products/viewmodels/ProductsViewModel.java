package mobi.liason.sample.products.viewmodels;

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

    public static final String VIEW_NAME = ProductsViewModel.class.getSimpleName();

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
        return ProductTable.TABLE_NAME + " ORDER BY " + Columns.NAME.getName();
    }

    @Override
    public List<Path> getPaths(Context context) {
        return Lists.newArrayList(Paths.PRODUCTS_VIEW_MODEL);
    }

    @Override
    public Cursor query(Context context, SQLiteDatabase sqLiteDatabase, Path path, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return super.query(context, sqLiteDatabase, path, uri, projection, selection, selectionArgs, sortOrder);
    }

    public static class Columns {
        public static final ViewModelColumn _ID = new ViewModelColumn(VIEW_NAME, BaseColumns._ID, ProductTable.Columns.ID);
        public static final ViewModelColumn NAME = new ViewModelColumn(VIEW_NAME, ProductTable.Columns.NAME);
        public static final ViewModelColumn IMAGE_THUMB_URL = new ViewModelColumn(VIEW_NAME, ProductTable.Columns.IMAGE_THUMB_URL);
        public static final Column[] COLUMNS = new Column[]{_ID, NAME, IMAGE_THUMB_URL};
    }

    public static class Paths {
        public static final Path PRODUCTS_VIEW_MODEL = new Path(VIEW_NAME);
    }


}

