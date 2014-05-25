package mobi.liason.sample.product.viewmodels;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

import mobi.liason.loaders.Path;
import mobi.liason.mvvm.database.Column;
import mobi.liason.mvvm.database.ViewModel;
import mobi.liason.mvvm.database.ViewModelColumn;
import mobi.liason.mvvm.network.TaskStateTable;
import mobi.liason.sample.models.ProductTable;
import mobi.liason.sample.overrides.SampleTaskService;
import mobi.liason.sample.product.tasks.ProductTask;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class ProductTaskStateViewModel extends ViewModel {

    public static final String VIEW_NAME = ProductTaskStateViewModel.class.getSimpleName();

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
        final String selection =
                TaskStateTable.TABLE_NAME +
                        " LEFT JOIN " +
                        ProductTable.TABLE_NAME +
                        " ON " +
                        TaskStateTable.TABLE_NAME + "." + TaskStateTable.Columns.URI.getName() +
                        " LIKE " +
                        "'%' || '" + ProductTask.PRODUCTS + "/' || " + ProductTable.Columns.ID.getName() + " || '%'" +
                        " WHERE " +
                        TaskStateTable.TABLE_NAME + "." + TaskStateTable.Columns.URI.getName() +
                        " LIKE " +
                        "'%" + ProductTask.PRODUCTS + "/%'";
        return selection;
    }

    @Override
    public List<Path> getPaths(Context context) {
        return Lists.newArrayList(Paths.PRODUCT_TASK_STATE);
    }

    @Override
    public Cursor query(Context context, SQLiteDatabase sqLiteDatabase, Path path, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final String uriString = uri.toString();

        final String overridenSelection = Columns.URI.getName() + "=?";
        final String[] overridenSelectionArguments = {uriString};

        final Cursor cursor = super.query(context, sqLiteDatabase, path, uri, projection, overridenSelection, overridenSelectionArguments, sortOrder);

        SampleTaskService.startTask(context, uri);

        return cursor;
    }

    public static class Columns {

        public static final ViewModelColumn URI = new ViewModelColumn(VIEW_NAME, TaskStateTable.Columns.URI);
        public static final ViewModelColumn ID = new ViewModelColumn(VIEW_NAME, ProductTable.Columns.ID);
        public static final ViewModelColumn STATE = new ViewModelColumn(VIEW_NAME, TaskStateTable.Columns.STATE);
        public static final ViewModelColumn JSON = new ViewModelColumn(VIEW_NAME, TaskStateTable.Columns.JSON);

        public static final String DATA_SELECTION = "CASE" +
                " WHEN " +
                ID.getName() + " NOT NULL " +
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

        public static final ViewModelColumn IS_PROGRESS_BAR_VISIBLE = new ViewModelColumn(VIEW_NAME, "isProgressBarVisible", PROGRESS_BAR_SELECTION, Column.Type.text);
        public static final ViewModelColumn IS_DATA_VISIBLE = new ViewModelColumn(VIEW_NAME, "isDataVisible", DATA_SELECTION, Column.Type.text);
        public static final Column[] COLUMNS = new Column[]{ID, URI, STATE, JSON, IS_PROGRESS_BAR_VISIBLE, IS_DATA_VISIBLE};
    }

    public static class Paths {
        public static final Path PRODUCT_TASK_STATE = ProductTask.Paths.PRODUCT;
    }

}

