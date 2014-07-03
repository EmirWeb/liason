package mobi.liason.sample.models;

import android.content.ContentValues;
import android.content.Context;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import mobi.liason.loaders.Path;
import mobi.liason.mvvm.database.Column;
import mobi.liason.mvvm.database.Model;
import mobi.liason.mvvm.database.ModelColumn;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class ProductTable extends Model {

    public static final String TABLE_NAME = ProductTable.class.getSimpleName();

    public static ContentValues getContentValues(final Product product) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.ID.getName(), product.getId());
        contentValues.put(Columns.NAME.getName(), product.getName());
        contentValues.put(Columns.IMAGE_THUMB_URL.getName(), product.getImageThumbUrl());
        contentValues.put(Columns.IMAGE_URL.getName(), product.getImageUrl());
        contentValues.put(Columns.DESCRIPTION.getName(), product.getDescription());
        contentValues.put(Columns.TASTING_NOTE.getName(), product.getTastingNote());
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

    @Override
    public Set<Column> getUniqueColumns(Context context) {
        return Sets.newHashSet(Columns.UNIQUE);
    }

    public static class Columns {
        public static final ModelColumn ID = new ModelColumn(TABLE_NAME, Product.Fields.ID, ModelColumn.Type.integer);
        public static final ModelColumn NAME = new ModelColumn(TABLE_NAME, Product.Fields.NAME, ModelColumn.Type.text);
        public static final ModelColumn IMAGE_THUMB_URL = new ModelColumn(TABLE_NAME, Product.Fields.IMAGE_THUMB_URL, ModelColumn.Type.text);
        public static final ModelColumn IMAGE_URL = new ModelColumn(TABLE_NAME, Product.Fields.IMAGE_URL, ModelColumn.Type.text);
        public static final ModelColumn DESCRIPTION = new ModelColumn(TABLE_NAME, Product.Fields.DESCRIPTION, ModelColumn.Type.text);
        public static final ModelColumn TASTING_NOTE = new ModelColumn(TABLE_NAME, Product.Fields.TASTING_NOTE + "EEE", ModelColumn.Type.text);
        public static final Column[] COLUMNS = new Column[]{ID, NAME, IMAGE_THUMB_URL, IMAGE_URL, DESCRIPTION, TASTING_NOTE};
        public static final Column[] UNIQUE = new Column[]{ID};
    }

    public static class Paths {
        public static final Path PRODUCT_TABLE = new Path(TABLE_NAME);
        public static final Path PRODUCT_TABLE_INSERT = new Path(TABLE_NAME, "#");
    }

    @Override
    public int getVersion(Context context) {
        return 1;
    }
}

