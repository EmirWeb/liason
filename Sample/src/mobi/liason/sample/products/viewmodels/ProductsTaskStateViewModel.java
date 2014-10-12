package mobi.liason.sample.products.viewmodels;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import mobi.liason.loaders.Path;
import mobi.liason.mvvm.database.Column;
import mobi.liason.mvvm.database.ViewModel;
import mobi.liason.mvvm.database.ViewModelColumn;
import mobi.liason.mvvm.database.annotations.ColumnDefinition;
import mobi.liason.mvvm.database.annotations.ColumnDefinitions;
import mobi.liason.mvvm.database.annotations.PathDefinition;
import mobi.liason.mvvm.database.annotations.PathDefinitions;
import mobi.liason.mvvm.task.TaskStateTable;
import mobi.liason.sample.models.ProductModel;
import mobi.liason.sample.overrides.SampleTaskService;
import mobi.liason.sample.products.tasks.ProductsTask;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class ProductsTaskStateViewModel extends ViewModel {

    public static final String VIEW_NAME = ProductsTaskStateViewModel.class.getSimpleName();

    @Override
    public String getName(final Context context) {
        return VIEW_NAME;
    }

    @Override
    protected String getSelection(Context context) {
        return TaskStateTable.TABLE_NAME + " WHERE " + TaskStateTable.TABLE_NAME + "." + TaskStateTable.Columns.URI.getName() + " LIKE '%" + ProductsTask.Paths.PRODUCTS.getPath() + "'";
    }

    @Override
    public Cursor query(Context context, SQLiteDatabase sqLiteDatabase, Path path, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SampleTaskService.startTask(context, uri);
        return super.query(context, sqLiteDatabase, path, uri, projection, selection, selectionArgs, sortOrder);
    }

    @ColumnDefinitions
    public static class Columns {
        @ColumnDefinition
        public static final ViewModelColumn URI = new ViewModelColumn(VIEW_NAME, TaskStateTable.Columns.URI);
        @ColumnDefinition
        public static final ViewModelColumn STATE = new ViewModelColumn(VIEW_NAME, TaskStateTable.Columns.STATE);
        @ColumnDefinition
        public static final ViewModelColumn JSON = new ViewModelColumn(VIEW_NAME, TaskStateTable.Columns.JSON);

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
                Columns.STATE.getName() + "='" + TaskStateTable.State.RUNNING + "'" +
                " THEN " +
                "'" + Boolean.toString(true) + "'" +
                " ELSE " +
                "'" + Boolean.toString(false) + "'" +
                " END ";

        @ColumnDefinition
        public static final ViewModelColumn IS_PROGRESS_BAR_VISIBLE = new ViewModelColumn(VIEW_NAME, "isProgressBarVisible", PROGRESS_BAR_SELECTION, Column.Type.text);
        @ColumnDefinition
        public static final ViewModelColumn IS_DATA_VISIBLE = new ViewModelColumn(VIEW_NAME, "isDataVisible", DATA_SELECTION, Column.Type.text);
    }

    @PathDefinitions
    public static class Paths {
        @PathDefinition
        public static final Path PRODUCTS_TASK_STATE = ProductsTask.Paths.PRODUCTS;
    }

}

