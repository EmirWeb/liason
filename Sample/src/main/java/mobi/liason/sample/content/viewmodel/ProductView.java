package mobi.liason.sample.content.viewmodel;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import mobi.liason.mvvm.database.Model;
import mobi.liason.mvvm.database.ModelColumn;
import mobi.liason.mvvm.database.Content;
import mobi.liason.sample.R;
import mobi.liason.sample.content.models.ProductTable;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class ProductView extends Model {

    private static final String CREATE = " CREATE TABLE IF EXISTS %s %s ; ";

    @Override
    public int getVersion(final Context context) {
        final Resources resources = context.getResources();
        return resources.getInteger(R.integer.product_table_version);
    }

    @Override
    public String getName(final Context context) {
        final Resources resources = context.getResources();
        return resources.getString(R.string.product_table_name);
    }

    public String getCreate(final Context context) {
        final String name = getName(context);
        final List<ModelColumn> modelColumns = getColumns(context);
        final String createColumns = ;


        final String create = String.format(CREATE, name, createColumns);
        return create;
    }

    @Override
    public List<String> getPaths(Context context) {
        final Resources resources = context.getResources();
        final String path = resources.getString(R.string.twitter_search_table_uri_path);

        final List<String> paths = new ArrayList<String>(1);
        paths.add(path);

        return paths;
    }

    public static class Columns {
        public static final ModelColumn ID = new ModelColumn(ProductTable.Columns.ID.get);
        public static final ModelColumn NAME = ProductTable.Columns.NAME;
        public static final ModelColumn IMAGE_THUMB_URL = ProductTable.Columns.IMAGE_THUMB_URL;
    }

}

