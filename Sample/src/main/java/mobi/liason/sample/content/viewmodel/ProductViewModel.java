package mobi.liason.sample.content.viewmodel;

import android.content.Context;
import android.content.res.Resources;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mobi.liason.mvvm.database.Column;
import mobi.liason.mvvm.database.ModelColumn;
import mobi.liason.mvvm.database.ViewModel;
import mobi.liason.mvvm.database.ViewModelColumn;
import mobi.liason.sample.R;
import mobi.liason.sample.content.models.ProductTable;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class ProductViewModel extends ViewModel{

    public static final String VIEW_NAME = "ProductView";
    private static final String SELECTION = ProductTable.TABLE_NAME;

    @Override
    public String getName(final Context context) {
        return VIEW_NAME;
    }

    @Override
    public List<Column> getColumns(Context context) {
        return Arrays.asList(Columns.COLUMNS);
    }

    @Override
    protected String getSelection(Context context) {
        return SELECTION;
    }

    @Override
    public List<String> getPaths(Context context) {
        return Lists.newArrayList(Paths.PATH);
    }

    public static class Columns {
        public static final ViewModelColumn ID = new ViewModelColumn(VIEW_NAME, ProductTable.Columns.ID);
        public static final ViewModelColumn NAME = new ViewModelColumn(VIEW_NAME, ProductTable.Columns.NAME);
        public static final ViewModelColumn IMAGE_THUMB_URL = new ViewModelColumn(VIEW_NAME, ProductTable.Columns.IMAGE_THUMB_URL);
        public static final Column[] COLUMNS = new Column[]{ID, NAME, IMAGE_THUMB_URL};
    }

    public static class Paths {
        public static final String PATH = "ProductViewModel";
    }

}

