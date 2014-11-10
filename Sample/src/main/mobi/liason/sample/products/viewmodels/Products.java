package mobi.liason.sample.products.viewmodels;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import mobi.liason.annotation.PathAction;
import mobi.liason.annotation.Projection;
import mobi.liason.annotation.Selection;
import mobi.liason.annotation.ViewModel;
import mobi.liason.annotation.Path;

import mobi.liason.mvvm.database.ViewModelColumn;
import mobi.liason.sample.models.ProductModel;
import mobi.liason.sample.products.viewmodels.ProductsViewModel;

@ViewModel
public class Products {

    @Projection
    public static final ViewModelColumn _ID = new ViewModelColumn(ProductsViewModel.NAME, BaseColumns._ID, ProductModel.Columns.ID);
    @Projection
    public static final ViewModelColumn NAME = new ViewModelColumn(ProductsViewModel.NAME, ProductModel.Columns.NAME);
    @Projection
    public static final ViewModelColumn IMAGE_THUMB_URL = new ViewModelColumn(ProductsViewModel.NAME, ProductModel.Columns.IMAGE_THUMB_URL);

    @Selection
    public static final String SELECTION = ProductModel.NAME + " ORDER BY " + NAME.getName();


}

