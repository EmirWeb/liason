package mobi.liason.loaders;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emir Hasanbegovic on 18/04/14.
 */
public class ContentVersionTable extends Content {

    public static final String TABLE_NAME = ContentVersionTable.class.getSimpleName();
    public static final int VERSION = 0;
    private static final String CREATE = "CREATE TABLE IF NOT EXISTS %s ( %s INTEGER, %s TEXT) ;";
    private static final String DROP = "DROP TABLE IF EXISTS ?%s;";

    @Override
    public int getVersion(Context context) {
        return VERSION;
    }

    @Override
    public String getName(Context context) {
        return TABLE_NAME;
    }

    @Override
    public String getCreate(Context context) {
        return String.format(CREATE, TABLE_NAME, Columns.VERSION, Columns.NAME);
    }

    @Override
    public String getDrop(Context context) {
        return String.format(DROP, TABLE_NAME);
    }

    @Override
    public List<Path> getPaths(Context context) {
        return new ArrayList<Path>();
    }

    public static class Columns {
        public static final String VERSION = "version";
        public static final String NAME = "name";
    }

    public static class Paths {
        public static final Path PATH = new Path(TABLE_NAME, "*");
    }

    @Override
    public Cursor query(Context context, SQLiteDatabase sqLiteDatabase, Path path, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final String contentName = uri.getLastPathSegment();
        final String overridenSelection = Columns.NAME + "=?";
        final String[] overidenSelectionArguments = {contentName};
        return super.query(context, sqLiteDatabase, path, uri, projection, overridenSelection, overidenSelectionArguments, sortOrder);
    }
}

