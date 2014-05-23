package mobi.liason.sample.content.models;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

import mobi.liason.mvvm.database.Column;
import mobi.liason.mvvm.database.Model;
import mobi.liason.mvvm.database.ModelColumn;
import mobi.liason.mvvm.providers.Path;
import mobi.liason.sample.services.TaskService;
import mobi.liason.sample.utilities.UriUtilities;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class TaskStateTable extends Model {

    public static final String TABLE_NAME = "TasksStateTable";

    public static ContentValues getContentValues(final TaskState taskState) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.URI.getName(), taskState.getUri());
        contentValues.put(Columns.STATE.getName(), taskState.getState());
        contentValues.put(Columns.TIME.getName(), taskState.getTime());
        final byte[] jsonObjectByteArray = taskState.getJsonByteArray();
        if (jsonObjectByteArray != null){
            contentValues.put(Columns.JSON.getName(), jsonObjectByteArray);
        }
        return contentValues;
    }

    @Override
    public String getName(final Context context) {
        return TABLE_NAME;
    }

    @Override
    public List<Column> getColumns(final Context context) {
        return Arrays.asList(Columns.COLUMNS);
    }

    @Override
    public List<Path> getPaths(Context context) {
        return Lists.newArrayList(Paths.TASK_STATE);
    }

    @Override
    public Cursor query(Context context, SQLiteDatabase sqLiteDatabase, Path path, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final String uriString = uri.getLastPathSegment();

        final String[] overridenProjection = {Columns.TIME.getName()};
        final String overridenSelection = Columns.URI.getName() + "=?";
        final String[] overridenSelectionArguments = {uriString};

        return super.query(context, sqLiteDatabase, path, uri, overridenProjection, overridenSelection, overridenSelectionArguments, null);
    }

    @Override
    public Uri insert(Context context, SQLiteDatabase sqLiteDatabase, Path path, Uri uri, ContentValues contentValues) {
        final String uriString = uri.getLastPathSegment();
        sqLiteDatabase.beginTransaction();
        try {
            final String selection = Columns.URI.getName() + "=? AND " + Columns.STATE.getName() + "<>?";
            final String[] selectionArguments = { uriString , State.RUNNING};

            final int rows = sqLiteDatabase.update(TaskStateTable.TABLE_NAME, contentValues, selection, selectionArguments);
            if (rows == 0) {
                final String queryWhereClause = Columns.URI.getName() + "=? AND " + Columns.STATE.getName() + "=?";
                final String[] queryWhereArguments = new String[]{uriString, State.RUNNING};
                final Cursor cursor = sqLiteDatabase.query(TaskStateTable.TABLE_NAME, null, queryWhereClause, queryWhereArguments, null, null, null);
                try {
                    if (cursor.getCount() != 0) {
                        return null;
                    }
                } finally {
                    cursor.close();
                }
                sqLiteDatabase.insert(TaskStateTable.TABLE_NAME, null, contentValues);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
        return UriUtilities.getUri(context, Paths.TASK_STATE, uriString);
    }

    public static final class QueryParameters {
        public static final String FORCE_TASK = "forceTask";
    }

    public static final class State {
        public static final String RUNNING = "running";
        public static final String SUCCESS = "success";
        public static final String FAIL = "fail";
    }

    public static class Columns {
        public static final ModelColumn URI = new ModelColumn(TABLE_NAME, TaskState.Fields.URI, ModelColumn.Type.text);
        public static final ModelColumn STATE = new ModelColumn(TABLE_NAME, TaskState.Fields.STATE, ModelColumn.Type.text);
        public static final ModelColumn TIME = new ModelColumn(TABLE_NAME, TaskState.Fields.TIME, ModelColumn.Type.integer);
        public static final ModelColumn JSON = new ModelColumn(TABLE_NAME, TaskState.Fields.JSON, ModelColumn.Type.blob);
        public static final Column[] COLUMNS = new Column[]{URI, STATE, TIME, JSON};
    }

    public static class Paths {
        public static final Path TASK_STATE = new Path(TABLE_NAME, "*");
    }

}

