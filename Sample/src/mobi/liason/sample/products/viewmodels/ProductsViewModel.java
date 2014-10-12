package mobi.liason.sample.products.viewmodels;

import android.content.Context;
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
public class ProductsViewModel extends ViewModel {

    public static final String VIEW_NAME = ProductsViewModel.class.getSimpleName();

    @Override
    public String getName(final Context context) {
        return VIEW_NAME;
    }

    @Override
    protected String getSelection(Context context) {
        return ProductModel.NAME + " ORDER BY " + Columns.NAME.getName();
    }

    @ColumnDefinitions
    public static class Columns {
        @ColumnDefinition
        public static final ViewModelColumn _ID = new ViewModelColumn(VIEW_NAME, BaseColumns._ID, ProductModel.Columns.ID);
        @ColumnDefinition
        public static final ViewModelColumn NAME = new ViewModelColumn(VIEW_NAME, ProductModel.Columns.NAME);
        @ColumnDefinition
        public static final ViewModelColumn IMAGE_THUMB_URL = new ViewModelColumn(VIEW_NAME, ProductModel.Columns.IMAGE_THUMB_URL);
    }

    @PathDefinitions
    public static class Paths {
        @PathDefinition
        public static final Path PRODUCTS_VIEW_MODEL = new Path(VIEW_NAME);
    }

}

