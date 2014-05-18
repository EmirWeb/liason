package mobi.liason.mvvm.providers;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mobi.liason.mvvm.database.Content;
import mobi.liason.mvvm.database.DatabaseHelper;

/**
 * Created by Emir Hasanbegovic on 18/04/14.
 */
public abstract class Provider extends ContentProvider {

    private final UriMatcher mURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private final Map<Integer, Content> mCodeContentMap = new HashMap<Integer, Content>();
    private final Map<Integer, Path> mCodePathMap = new HashMap<Integer, Path>();
    private DatabaseHelper mDatabaseHelper;

    public abstract String getAuthority(final Context context);

    protected abstract DatabaseHelper onCreateDatabaseHelper(final Context context);

    protected SQLiteDatabase getSQLiteDatabase(final Context context) {
        final DatabaseHelper databaseHelper = getDatabaseHelper(context);
        return databaseHelper.getWritableDatabase();
    }

    protected synchronized DatabaseHelper getDatabaseHelper(final Context context) {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = onCreateDatabaseHelper(context);
        }
        return mDatabaseHelper;
    }

    protected List<Content> getContent(final Context context) {
        final DatabaseHelper databaseHelper = getDatabaseHelper(context);
        return databaseHelper.getContent(context);
    }

    @Override
    public boolean onCreate() {
        final Context context = getContext();
        final List<Content> contents = getContent(context);
        final String authority = getAuthority(context);

        int index = 0;
        for (final Content content : contents) {
            final List<Path> paths = content.getPaths(context);
            for (final Path path : paths) {
                mCodeContentMap.put(index, content);
                mCodePathMap.put(index, path);
                mURIMatcher.addURI(authority, path.getMatcherPath(), index);
                index++;
            }
        }

        return true;
    }

    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        final int code = mURIMatcher.match(uri);
        if (code == UriMatcher.NO_MATCH) {
            return null;
        }

        final Context context = getContext();
        final SQLiteDatabase sqLiteDatabase = getSQLiteDatabase(context);
        final Content content = mCodeContentMap.get(code);
        final Path path = mCodePathMap.get(code);
        return content.query(context, sqLiteDatabase, path, uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public String getType(final Uri uri) {
        final int code = mURIMatcher.match(uri);
        if (code == UriMatcher.NO_MATCH) {
            return null;
        }

        final Context context = getContext();
        final SQLiteDatabase sqLiteDatabase = getSQLiteDatabase(context);
        final Content content = mCodeContentMap.get(code);
        final Path path = mCodePathMap.get(code);
        return content.type(context, sqLiteDatabase, path, uri);
    }

    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        final int code = mURIMatcher.match(uri);
        if (code == UriMatcher.NO_MATCH) {
            return null;
        }

        final Context context = getContext();
        final SQLiteDatabase sqLiteDatabase = getSQLiteDatabase(context);
        final Content content = mCodeContentMap.get(code);
        final Path path = mCodePathMap.get(code);
        return content.insert(context, sqLiteDatabase, path, uri, values);
    }

    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        final int code = mURIMatcher.match(uri);
        if (code == UriMatcher.NO_MATCH) {
            return 0;
        }

        final Context context = getContext();
        final SQLiteDatabase sqLiteDatabase = getSQLiteDatabase(context);
        final Content content = mCodeContentMap.get(code);
        final Path path = mCodePathMap.get(code);
        return content.delete(context, sqLiteDatabase, path, uri, selection, selectionArgs);
    }

    @Override
    public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        final int code = mURIMatcher.match(uri);
        if (code == UriMatcher.NO_MATCH) {
            return 0;
        }

        final Context context = getContext();
        final SQLiteDatabase sqLiteDatabase = getSQLiteDatabase(context);
        final Content content = mCodeContentMap.get(code);
        final Path path = mCodePathMap.get(code);
        return content.update(context, sqLiteDatabase, path, uri, values, selection, selectionArgs);
    }

    @Override
    public ContentProviderResult[] applyBatch(final ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        final Context context = getContext();
        final SQLiteDatabase sqLiteDatabase = getSQLiteDatabase(context);
        sqLiteDatabase.beginTransaction();
        try {
            final ContentProviderResult[] contentProviderResults = super.applyBatch(operations);
            sqLiteDatabase.setTransactionSuccessful();
            return contentProviderResults;
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

}

