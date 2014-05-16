package mobi.liason.sample;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import mobi.liason.mvvm.database.Content;
import mobi.liason.mvvm.database.DatabaseHelper;
import mobi.liason.sample.content.models.ProductTable;
import mobi.liason.sample.content.viewmodel.ProductViewModel;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class SampleDatabaseHelper extends DatabaseHelper {

    private static final String DATA_BASE_NAME = "SampleDataBaseName";
    private static final int VERSION = 1;

    public SampleDatabaseHelper(Context context) {
        super(context, DATA_BASE_NAME, VERSION);
    }

    @Override
    public List<Content> getContent(final Context context) {
        final List<Content> contentList = new ArrayList<Content>();
        contentList.add(new ProductTable());
        contentList.add(new ProductViewModel());
        return contentList;
    }

    private static SampleDatabaseHelper sSampleDataBaseHelper;

    public static synchronized DatabaseHelper getDatabaseHelper(final Context context){
        if (sSampleDataBaseHelper == null){
            sSampleDataBaseHelper = new SampleDatabaseHelper(context);
        }
        return sSampleDataBaseHelper;
    }
}
