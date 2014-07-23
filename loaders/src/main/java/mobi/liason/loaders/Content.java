package mobi.liason.loaders;

import android.content.ContentResolver;
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

    public abstract int getVersion(final Context context);

    public abstract String getName(final Context context);

    public abstract String getCreate(final Context context);

    public abstract String getDrop(final Context context);

    public abstract List<Path> getPaths(final Context context);

    public void onUpgrade(final Context context, final SQLiteDatabase sqLiteDatabase, final int oldVersion) {
        final int currentVersion = getVersion(context);
        if (oldVersion != currentVersion) {
            final String drop = getDrop(context);
            final String create = getCreate(context);
            sqLiteDatabase.execSQL(drop);
            sqLiteDatabase.execSQL(create);
        }
    }

    public Cursor query(final Context context, final SQLiteDatabase sqLiteDatabase, final Path path, final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        final String name = getName(context);
        return sqLiteDatabase.query(name, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public String type(final Context context, final SQLiteDatabase sqLiteDatabase, final Path path, final Uri uri) {
        return ContentResolver.CURSOR_ITEM_BASE_TYPE;
    }

    public Uri insert(final Context context, final SQLiteDatabase sqLiteDatabase, final Path path, final Uri uri, final ContentValues values) {
        final String name = getName(context);
        final long id = sqLiteDatabase.insert(name, null, values);

        final String authority = uri.getAuthority();
        final String scheme = uri.getScheme();

        try {
            return UriUtilities.getUri(scheme, authority, path, id);
        } catch (final Exception exception){
        }

        final Uri insertUri = UriUtilities.getUri(scheme, authority, path);
        return insertUri.buildUpon().appendPath(Long.toString(id)).build();
    }

    public int delete(final Context context, final SQLiteDatabase sqLiteDatabase, final Path path, final Uri uri, final String selection, final String[] selectionArgs) {
        final String name = getName(context);
        return sqLiteDatabase.delete(name, selection, selectionArgs);
    }

    public int update(final Context context, final SQLiteDatabase sqLiteDatabase, final Path path, final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        final String name = getName(context);
        return sqLiteDatabase.update(name, values, selection, selectionArgs);
    }

    public int bulkInsert(final Context context, final SQLiteDatabase sqLiteDatabase, final Path path, final Uri uri, final ContentValues[] contentValuesArray) {
        final String name = getName(context);

        try {
            sqLiteDatabase.beginTransaction();
            int writeCount = 0;
            for (final ContentValues contentValues : contentValuesArray) {
                final long id = sqLiteDatabase.insert(name, null, contentValues);
                if (id != -1) {
                    writeCount++;
                }
            }
            sqLiteDatabase.setTransactionSuccessful();
            return writeCount;
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }
}

