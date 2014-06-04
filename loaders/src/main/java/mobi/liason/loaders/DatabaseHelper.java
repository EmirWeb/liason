package mobi.liason.loaders;

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

    private static final String SCHEME = "content";
    private static final String AUTHORITY = "mobi.liason";
    private final Context mContext;

    public DatabaseHelper(final Context context, final String name, final int version) {
        super(context, name, null, version);
        mContext = context;
    }

    public abstract List<Content> getContent(final Context context);

    @Override
    public void onOpen(final SQLiteDatabase sqLiteDatabase) {
        final List<Content> contentList = getContent(mContext);
        for (final Content content : contentList) {
            final int newVersion = content.getVersion(mContext);

            if (newVersion == -1) {
                content.onUpgrade(mContext, sqLiteDatabase, -1);
            } else {
                final String contentName = content.getName(mContext);
                final Uri uri = UriUtilities.getUri(SCHEME, AUTHORITY, ContentVersionTable.Paths.PATH, contentName);
                final String[] projection = {ContentVersionTable.Columns.VERSION};
                final Cursor cursor = content.query(mContext, sqLiteDatabase, ContentVersionTable.Paths.PATH, uri, projection, null, null, null);

                final int versionColumnIndex = cursor.getColumnIndex(ContentVersionTable.Columns.VERSION);
                if (versionColumnIndex != -1) {
                    final int oldVersion = cursor.getInt(versionColumnIndex);
                    if (newVersion != oldVersion) {
                        content.onUpgrade(mContext, sqLiteDatabase, oldVersion);
                    }
                }
                cursor.close();
            }
        }
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        final List<Content> contentList = getContent(mContext);
        contentList.add(new ContentVersionTable());
        for (final Content content : contentList) {
            final String create = content.getCreate(mContext);
            sqLiteDatabase.execSQL(create);
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
