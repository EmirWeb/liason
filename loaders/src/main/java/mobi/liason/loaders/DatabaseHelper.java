package mobi.liason.loaders;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.List;

/**
 * Created by Emir Hasanbegovic on 18/04/14.
 */
public abstract class DatabaseHelper extends SQLiteOpenHelper {

    private static final String AUTHORITY = "mobi.liason";
    private final Context mContext;

    public DatabaseHelper(final Context context, final String name, final int version) {
        super(context, name, null, version);
        mContext = context;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        final SQLiteDatabase writableDatabase = super.getWritableDatabase();
        return writableDatabase;
    }

    public abstract List<Content> getContent(final Context context);

    @Override
    public void onOpen(final SQLiteDatabase sqLiteDatabase) {
        if (sqLiteDatabase.isReadOnly()){
            return;
        }

        final ContentVersionTable contentVersionTable = new ContentVersionTable();

        final List<Content> contentList = getContent(mContext);
        for (final Content content : contentList) {
            final int newVersion = content.getVersion(mContext);

            final String contentName = content.getName(mContext);
            final Uri uri = UriUtilities.getUri(ContentResolver.SCHEME_CONTENT, AUTHORITY, ContentVersionTable.Paths.PATH, contentName);
            final String[] projection = {ContentVersionTable.Columns.VERSION};
            final Cursor cursor = contentVersionTable.query(mContext, sqLiteDatabase, ContentVersionTable.Paths.PATH, uri, projection, null, null, null);

            if (cursor.moveToFirst()) {
                final int versionColumnIndex = cursor.getColumnIndex(ContentVersionTable.Columns.VERSION);
                if (versionColumnIndex != -1) {
                    final int oldVersion = cursor.getInt(versionColumnIndex);
                    if (newVersion != oldVersion) {
                        content.onUpgrade(mContext, sqLiteDatabase, oldVersion);

                        final ContentValues contentValues = new ContentValues();
                        contentValues.put(ContentVersionTable.Columns.VERSION, newVersion);
                        final Uri insertUri = contentVersionTable.insert(mContext, sqLiteDatabase, ContentVersionTable.Paths.PATH, uri, contentValues);
                    }
                }
            }
            cursor.close();

        }
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        final ContentVersionTable contentVersionTable = new ContentVersionTable();
        final String contentVersionTableCreate = contentVersionTable.getCreate(mContext);
        sqLiteDatabase.execSQL(contentVersionTableCreate);

        final List<Content> contentList = getContent(mContext);
        for (final Content content : contentList) {
            final String create = content.getCreate(mContext);
            sqLiteDatabase.execSQL(create);

            final int newVersion = content.getVersion(mContext);
            final String contentName = content.getName(mContext);
            final Uri uri = UriUtilities.getUri(ContentResolver.SCHEME_CONTENT, AUTHORITY, ContentVersionTable.Paths.PATH, contentName);
            final ContentValues contentValues = new ContentValues();
            contentValues.put(ContentVersionTable.Columns.VERSION, newVersion);
            final Uri insertUri = contentVersionTable.insert(mContext, sqLiteDatabase, ContentVersionTable.Paths.PATH, uri, contentValues);

        }
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int oldVersion, final int newVersion) {
        if (oldVersion != newVersion) {
            final List<Content> contentList = getContent(mContext);
            for (final Content content : contentList) {
                final String drop = content.getDrop(mContext);
                final String create = content.getCreate(mContext);
                sqLiteDatabase.execSQL(drop);
                sqLiteDatabase.execSQL(create);
            }
        } else if (oldVersion == newVersion) {

        }
    }
}
