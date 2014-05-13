package mobi.liason.sample;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import mobi.liason.mvvm.database.Content;
import mobi.liason.mvvm.database.DatabaseHelper;
import mobi.liason.sample.content.models.ProductTable;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class SampleDatabaseHelper extends DatabaseHelper {

    public SampleDatabaseHelper(Context context, String name, int version) {
        super(context, name, version);
    }

    @Override
    public List<Content> getContent(final Context context) {
        final List<Content> contentList = new ArrayList<Content>();
        contentList.add( new ProductTable());
        return contentList;
    }
}
