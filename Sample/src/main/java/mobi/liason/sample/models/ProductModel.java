package mobi.liason.sample.models;

import android.content.ContentValues;
    import android.content.Context;

import mobi.liason.loaders.Path;
import mobi.liason.mvvm.database.Model;
import mobi.liason.mvvm.database.ModelColumn;
import mobi.liason.mvvm.database.annotations.ColumnDefinition;
import mobi.liason.mvvm.database.annotations.ColumnDefinitions;
import mobi.liason.mvvm.database.annotations.PathDefinition;
import mobi.liason.mvvm.database.annotations.PathDefinitions;
import mobi.liason.mvvm.database.annotations.Unique;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class ProductModel extends Model {

    public static final String TABLE_NAME = ProductModel.class.getSimpleName();

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

    @ColumnDefinitions
    public static class Columns {
        @ColumnDefinition
        @Unique
        public static final ModelColumn ID = new ModelColumn(TABLE_NAME, Product.Fields.ID, ModelColumn.Type.integer);
        @ColumnDefinition
        public static final ModelColumn NAME = new ModelColumn(TABLE_NAME, Product.Fields.NAME, ModelColumn.Type.text);
        @ColumnDefinition
        public static final ModelColumn IMAGE_THUMB_URL = new ModelColumn(TABLE_NAME, Product.Fields.IMAGE_THUMB_URL, ModelColumn.Type.text);
        @ColumnDefinition
        public static final ModelColumn IMAGE_URL = new ModelColumn(TABLE_NAME, Product.Fields.IMAGE_URL, ModelColumn.Type.text);
        @ColumnDefinition
        public static final ModelColumn DESCRIPTION = new ModelColumn(TABLE_NAME, Product.Fields.DESCRIPTION, ModelColumn.Type.text);
        @ColumnDefinition
        public static final ModelColumn TASTING_NOTE = new ModelColumn(TABLE_NAME, Product.Fields.TASTING_NOTE, ModelColumn.Type.text);
    }

    @PathDefinitions
    public static class Paths {
        @PathDefinition
        public static final Path PRODUCT_TABLE = new Path(TABLE_NAME);
    }

    @Override
    public int getVersion(Context context) {
        return 2;
    }
}

