package mobi.liason.sample.content.models;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import mobi.liason.mvvm.database.Model;
import mobi.liason.mvvm.database.ModelColumn;
import mobi.liason.sample.R;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class ProductTable extends Model {

    public static ContentValues getContentValues(final Product product) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.ID.getName(), product.getId());
        contentValues.put(Columns.NAME.getName(), product.getName());
        contentValues.put(Columns.IMAGE_THUMB_URL.getName(), product.getImageThumbUrl());
        contentValues.put(Columns.IMAGE_URL.getName(), product.getImageUrl());
        return contentValues;
    }

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

    @Override
    public List<ModelColumn> getColumns(final Context context) {
        final List<ModelColumn> modelColumns = new ArrayList<ModelColumn>();
        modelColumns.add(Columns.ID);
        modelColumns.add(Columns.NAME);
        modelColumns.add(Columns.IMAGE_THUMB_URL);
        modelColumns.add(Columns.IMAGE_URL);
        return modelColumns;
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
        public static final ModelColumn ID = new ModelColumn(Product.Fields.ID, ModelColumn.Type.integer);
        public static final ModelColumn NAME = new ModelColumn(Product.Fields.NAME, ModelColumn.Type.text);
        public static final ModelColumn IMAGE_THUMB_URL = new ModelColumn(Product.Fields.IMAGE_THUMB_URL, ModelColumn.Type.text);
        public static final ModelColumn IMAGE_URL = new ModelColumn(Product.Fields.IMAGE_URL, ModelColumn.Type.text);
    }

}

