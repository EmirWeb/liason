package mobi.liason.sample.products.viewmodels;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import mobi.liason.annotation.Path;
import mobi.liason.annotation.ViewModel;
import mobi.liason.annotation.PathAction;
import mobi.liason.annotation.Projection;
import mobi.liason.annotation.Selection;

import mobi.liason.sample.products.viewmodels.ProductsTaskStateViewModel;
import mobi.liason.mvvm.database.Column;
import mobi.liason.mvvm.database.ViewModelColumn;
import mobi.liason.mvvm.task.TaskStateTable;
import mobi.liason.sample.models.ProductModel;
import mobi.liason.sample.overrides.SampleTaskService;
import mobi.liason.sample.products.tasks.ProductsTask;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
@ViewModel
public class ProductsTaskState {

    @Selection
    public static final String SELECTION = TaskStateTable.TABLE_NAME + " WHERE " + TaskStateTable.TABLE_NAME + "." + TaskStateTable.Columns.URI.getName() + " LIKE '%" + ProductsTask.Paths.PRODUCTS.getPath() + "'";

    @PathAction(value = "ProductsTaskState", pathType = PathAction.PathType.query)
    public static Cursor query(Context context, SQLiteDatabase sqLiteDatabase, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SampleTaskService.startTask(context, uri);
        return null;
    }

    @Projection
    public static final ViewModelColumn URI = new ViewModelColumn(ProductsTaskStateViewModel.NAME, TaskStateTable.Columns.URI);
    @Projection
    public static final ViewModelColumn STATE = new ViewModelColumn(ProductsTaskStateViewModel.NAME, TaskStateTable.Columns.STATE);
    @Projection
    public static final ViewModelColumn JSON = new ViewModelColumn(ProductsTaskStateViewModel.NAME, TaskStateTable.Columns.JSON);

    public static final String DATA_SELECTION = "CASE" +
            " WHEN " +
            "(SELECT COUNT(*) FROM " + ProductModel.NAME + ") > 0" +
            " THEN " +
            "'" + Boolean.toString(true) + "'" +
            " ELSE " +
            "'" + Boolean.toString(false) + "'" +
            " END";
    public static final String PROGRESS_BAR_SELECTION = "CASE" +
            " WHEN " +
            STATE.getName() + "='" + TaskStateTable.State.RUNNING + "'" +
            " THEN " +
            "'" + Boolean.toString(true) + "'" +
            " ELSE " +
            "'" + Boolean.toString(false) + "'" +
            " END ";

    @Projection
    public static final ViewModelColumn IS_PROGRESS_BAR_VISIBLE = new ViewModelColumn(ProductsTaskStateViewModel.NAME, "isProgressBarVisible", PROGRESS_BAR_SELECTION, Column.Type.text);
    @Projection
    public static final ViewModelColumn IS_DATA_VISIBLE = new ViewModelColumn(ProductsTaskStateViewModel.NAME, "isDataVisible", DATA_SELECTION, Column.Type.text);



}

