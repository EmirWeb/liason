package mobi.liason.mvvm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by Emir Hasanbegovic on 18/04/14.
 */
public abstract class DatabaseHelper extends SQLiteOpenHelper {

    private final Context mContext;

    public DatabaseHelper(final Context context, final String name, final int version) {
        super(context, name, null, version);
        mContext = context;
    }

    public abstract List<Content> getContent(final Context context);

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        final List<Content> contentList = getContent(mContext);
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
        }
    }
}
