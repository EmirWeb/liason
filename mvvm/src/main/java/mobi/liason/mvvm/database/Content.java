package mobi.liason.mvvm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.List;

/**
 * Created by Emir Hasanbegovic on 18/04/14.
 */
public abstract class Content {
    private static final String DROP = " DROP TABLE IF EXISTS %s; ";

    public abstract int getVersion(final Context context);

    public abstract String getName(final Context context);

    public abstract String getCreate(final Context context);

    public abstract Uri getUri(final Context context);

    public String getDrop(final Context context) {
        final String name = getName(context);
        final String drop = String.format(DROP, name);
        return drop;
    }

    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final Context context, final int oldVersion) {
        final int currentVersion = getVersion(context);
        if (oldVersion != currentVersion) {
            final String drop = getDrop(context);
            final String create = getCreate(context);
            sqLiteDatabase.execSQL(drop);
            sqLiteDatabase.execSQL(create);
        }
    }

    public abstract List<String> getPaths(final Context context);

    public Cursor query(final Context context, final SQLiteDatabase sqLiteDatabase, final String path, final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        final String name = getName(context);
        return sqLiteDatabase.query(name, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public abstract String type(final Context context, final SQLiteDatabase sqLiteDatabase, final String path, final Uri uri);

    public Uri insert(final Context context, final SQLiteDatabase sqLiteDatabase, final String path, final Uri uri, final ContentValues values) {
        final String name = getName(context);
        final long id = sqLiteDatabase.insert(name, null, values);
        final Uri insertUri = getUri(context);
        return insertUri.buildUpon().appendPath(Long.toString(id)).build();
    }

    public int delete(final Context context, final SQLiteDatabase sqLiteDatabase, final String path, final Uri uri, final String selection, final String[] selectionArgs) {
        final String name = getName(context);
        return sqLiteDatabase.delete(name, selection, selectionArgs);
    }

    public int update(final Context context, final SQLiteDatabase sqLiteDatabase, final String path, final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        final String name = getName(context);
        return sqLiteDatabase.update(name, values, selection, selectionArgs);
    }
}

