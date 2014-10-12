package mobi.liason.sample.product.viewmodels;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import mobi.liason.loaders.Path;
import mobi.liason.mvvm.database.ViewModel;
import mobi.liason.mvvm.database.ViewModelColumn;
import mobi.liason.mvvm.database.annotations.ColumnDefinition;
import mobi.liason.mvvm.database.annotations.ColumnDefinitions;
import mobi.liason.mvvm.database.annotations.PathDefinition;
import mobi.liason.mvvm.database.annotations.PathDefinitions;
import mobi.liason.sample.models.ProductModel;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class ProductViewModel extends ViewModel {

    public static final String VIEW_NAME = ProductViewModel.class.getSimpleName();

    @Override
    public String getName(final Context context) {
        return VIEW_NAME;
    }

    @Override
    protected String getSelection(Context context) {
        return  ProductModel.NAME;
    }

    @Override
    public Cursor query(Context context, SQLiteDatabase sqLiteDatabase, Path path, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final String lastPathSegment = uri.getLastPathSegment();
        final String overridenSelection = Columns._ID.getName() + "=?";
        final String[] overridenSelectionArguments = {lastPathSegment};
        final Cursor cursor = super.query(context, sqLiteDatabase, path, uri, projection, overridenSelection, overridenSelectionArguments, sortOrder);
        return cursor;
    }

    @ColumnDefinitions
    public static class Columns {
        @ColumnDefinition
        public static final ViewModelColumn _ID = new ViewModelColumn(VIEW_NAME, BaseColumns._ID, ProductModel.Columns.ID);
        @ColumnDefinition
        public static final ViewModelColumn NAME = new ViewModelColumn(VIEW_NAME, ProductModel.Columns.NAME);
        @ColumnDefinition
        public static final ViewModelColumn IMAGE_URL = new ViewModelColumn(VIEW_NAME, ProductModel.Columns.IMAGE_URL);
        @ColumnDefinition
        public static final ViewModelColumn TASTING_NOTE = new ViewModelColumn(VIEW_NAME, ProductModel.Columns.TASTING_NOTE);
    }

    @PathDefinitions
    public static class Paths {
        @PathDefinition
        public static final Path PRODUCT_VIEW_MODEL = new Path(VIEW_NAME, "#");
    }

    @Override
    public int getVersion(Context context) {
        return 0;
    }
}

