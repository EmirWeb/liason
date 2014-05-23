package mobi.liason.sample.content.models;

import android.content.ContentValues;
import android.content.Context;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

import mobi.liason.loaders.Path;
import mobi.liason.mvvm.database.Column;
import mobi.liason.mvvm.database.Model;
import mobi.liason.mvvm.database.ModelColumn;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class ProductTable extends Model {

    public static final String TABLE_NAME = "ProductTable";

    public static ContentValues getContentValues(final Product product) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.ID.getName(), product.getId());
        contentValues.put(Columns.NAME.getName(), product.getName());
        contentValues.put(Columns.IMAGE_THUMB_URL.getName(), product.getImageThumbUrl());
        contentValues.put(Columns.IMAGE_URL.getName(), product.getImageUrl());
        return contentValues;
    }

    @Override
    public String getName(final Context context) {
        return TABLE_NAME;
    }

    @Override
    public List<Column> getColumns(final Context context) {
        return Arrays.asList(Columns.COLUMNS);
    }

    @Override
    public List<Path> getPaths(Context context) {
        return Lists.newArrayList(Paths.PRODUCT_TABLE);
    }

    public static class Columns {
        public static final ModelColumn ID = new ModelColumn(TABLE_NAME, Product.Fields.ID, ModelColumn.Type.integer);
        public static final ModelColumn NAME = new ModelColumn(TABLE_NAME, Product.Fields.NAME, ModelColumn.Type.text);
        public static final ModelColumn IMAGE_THUMB_URL = new ModelColumn(TABLE_NAME, Product.Fields.IMAGE_THUMB_URL, ModelColumn.Type.text);
        public static final ModelColumn IMAGE_URL = new ModelColumn(TABLE_NAME, Product.Fields.IMAGE_URL, ModelColumn.Type.text);
        public static final Column[] COLUMNS = new Column[]{ID, NAME, IMAGE_THUMB_URL, IMAGE_URL};
    }

    public static class Paths {
        public static final Path PRODUCT_TABLE = new Path(TABLE_NAME);
    }

}

