package mobi.liason.annotation;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;

import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;

import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;
import static java.util.Arrays.asList;
import static org.truth0.Truth.ASSERT;

/**
 * Created by Emir Hasanbegovic on 09/10/14.
 */
@RunWith(JUnit4.class)
public class ModelCreatorTest {
    static final Iterable<? extends Processor> MODEL_PROCESSORS(){
        return Collections.singletonList(new ModelProcessor());
    }


    @Test
    public void CreatesEmptyModel() {
        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "import mobi.liason.annotation.Model;",
                "@Model",
                "public class Product {",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductModel", Joiner.on("\n").join(
                "import android.content.Context;",
                "import mobi.liason.loaders.Path;",
                "import mobi.liason.mvvm.database.Model;",
                "import mobi.liason.mvvm.database.annotations.PathDefinition;",
                "import mobi.liason.mvvm.database.annotations.PathDefinitions;",
                "public class ProductModel extends Model {",
                "   public static final String NAME = ProductModel.class.getSimpleName();",
                "   @Override",
                "   public String getName(final Context context) {",
                "       return NAME;",
                "   }",
                "   @PathDefinitions",
                "   public static class Paths {",
                "       @PathDefinition",
                "       public static final Path PRODUCT = new Path(ProductModel.NAME);",
                "   }",
                "}"));

        ASSERT.about(javaSources())
                .that(asList(metaJavaFileObject))
                .processedWith(MODEL_PROCESSORS())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedJavaFileObject);
    }

    @Test
    public void createModelWithTextVariable() {
        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "package mobi.liason.test;",
                "import mobi.liason.annotation.Model;",
                "import mobi.liason.annotation.Text;",
                "@Model",
                "public class Product {",
                "   @Text",
                "   public static final String ID = \"id\";",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductModel", Joiner.on("\n").join(
                "package mobi.liason.test;",
                "import android.content.ContentValues;",
                "import android.content.Context;",
                "import mobi.liason.loaders.Path;",
                "import mobi.liason.mvvm.database.Column;",
                "import mobi.liason.mvvm.database.Model;",
                "import mobi.liason.mvvm.database.ModelColumn;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinition;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinitions;",
                "import mobi.liason.mvvm.database.annotations.PathDefinition;",
                "import mobi.liason.mvvm.database.annotations.PathDefinitions;",
                "import mobi.liason.test.ProductJson;",
                "public class ProductModel extends Model {",
                "   public static final String NAME = ProductModel.class.getSimpleName();",
                "   @Override",
                "   public String getName(final Context context) {",
                "       return NAME;",
                "   }",
                "   public static ContentValues getContentValues(final ProductJson productJson) {",
                "       final ContentValues contentValues = new ContentValues();",
                "       contentValues.put(Columns.ID.getName(), productJson.getId());",
                "       return contentValues;",
                "   }",
                "   @ColumnDefinitions",
                "   public static class Columns {",
                "       @ColumnDefinition",
                "       public static final ModelColumn ID = new ModelColumn(ProductModel.NAME, Product.ID, Column.Type.text);",
                "   }",
                "   @PathDefinitions",
                "   public static class Paths {",
                "       @PathDefinition",
                "       public static final Path PRODUCT = new Path(ProductModel.NAME);",
                "   }",
                "}"));

        ASSERT.about(javaSources())
                .that(asList(metaJavaFileObject))
                .processedWith(MODEL_PROCESSORS())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedJavaFileObject);
    }

    @Test
    public void createModelWithUniqueTextVariable() {
        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "package mobi.liason.test;",
                "import mobi.liason.annotation.Model;",
                "import mobi.liason.annotation.Text;",
                "import mobi.liason.mvvm.database.annotations.Unique;",
                "@Model",
                "public class Product {",
                "   @Unique",
                "   @Text",
                "   public static final String ID = \"id\";",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductModel", Joiner.on("\n").join(
                "package mobi.liason.test;",
                "import android.content.ContentValues;",
                "import android.content.Context;",
                "import mobi.liason.loaders.Path;",
                "import mobi.liason.mvvm.database.Column;",
                "import mobi.liason.mvvm.database.Model;",
                "import mobi.liason.mvvm.database.ModelColumn;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinition;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinitions;",
                "import mobi.liason.mvvm.database.annotations.PathDefinition;",
                "import mobi.liason.mvvm.database.annotations.PathDefinitions;",
                "import mobi.liason.mvvm.database.annotations.Unique;",
                "import mobi.liason.test.ProductJson;",
                "public class ProductModel extends Model {",
                "   public static final String NAME = ProductModel.class.getSimpleName();",
                "   @Override",
                "   public String getName(final Context context) {",
                "       return NAME;",
                "   }",
                "   public static ContentValues getContentValues(final ProductJson productJson) {",
                "       final ContentValues contentValues = new ContentValues();",
                "       contentValues.put(Columns.ID.getName(), productJson.getId());",
                "       return contentValues;",
                "   }",
                "   @ColumnDefinitions",
                "   public static class Columns {",
                "       @ColumnDefinition",
                "       @Unique",
                "       public static final ModelColumn ID = new ModelColumn(ProductModel.NAME, Product.ID, Column.Type.text);",
                "   }",
                "   @PathDefinitions",
                "   public static class Paths {",
                "       @PathDefinition",
                "       public static final Path PRODUCT = new Path(ProductModel.NAME);",
                "   }",
                "}"));

        ASSERT.about(javaSources())
                .that(asList(metaJavaFileObject))
                .processedWith(MODEL_PROCESSORS())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedJavaFileObject);
    }

    @Test
    public void createModelWithPrimitiveObjectVariable() {
        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "package mobi.liason.test;",
                "import mobi.liason.annotation.Model;",
                "import mobi.liason.annotation.Object;",
                "@Model",
                "public class Product {",
                "   @Object(\"java.lang.String\")",
                "   public static final String ID = \"id\";",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductModel", Joiner.on("\n").join(
                "package mobi.liason.test;",
                "import android.content.ContentValues;",
                "import android.content.Context;",
                "import mobi.liason.loaders.Path;",
                "import mobi.liason.mvvm.database.Column;",
                "import mobi.liason.mvvm.database.Model;",
                "import mobi.liason.mvvm.database.ModelColumn;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinition;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinitions;",
                "import mobi.liason.mvvm.database.annotations.PathDefinition;",
                "import mobi.liason.mvvm.database.annotations.PathDefinitions;",
                "import mobi.liason.test.ProductJson;",
                "public class ProductModel extends Model {",
                "   public static final String NAME = ProductModel.class.getSimpleName();",
                "   @Override",
                "   public String getName(final Context context) {",
                "       return NAME;",
                "   }",
                "   public static ContentValues getContentValues(final ProductJson productJson) {",
                "       final ContentValues contentValues = new ContentValues();",
                "       contentValues.put(Columns.ID.getName(), productJson.getId());",
                "       return contentValues;",
                "   }",
                "   @ColumnDefinitions",
                "   public static class Columns {",
                "       @ColumnDefinition",
                "       public static final ModelColumn ID = new ModelColumn(ProductModel.NAME, Product.ID, Column.Type.text);",
                "   }",
                "   @PathDefinitions",
                "   public static class Paths {",
                "       @PathDefinition",
                "       public static final Path PRODUCT = new Path(ProductModel.NAME);",
                "   }",
                "}"));

        ASSERT.about(javaSources())
                .that(asList(metaJavaFileObject))
                .processedWith(MODEL_PROCESSORS())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedJavaFileObject);
    }

    @Test
    public void createModelWitNonPrimitiveObjectVariable() {
        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "package mobi.liason.test;",
                "import mobi.liason.annotation.Model;",
                "import mobi.liason.annotation.Object;",
                "@Model",
                "public class Product {",
                "   @Object(\"java.lang.String\")",
                "   public static final String ID = \"id\";",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductModel", Joiner.on("\n").join(
                "package mobi.liason.test;",
                "import android.content.ContentValues;",
                "import android.content.Context;",
                "import mobi.liason.loaders.Path;",
                "import mobi.liason.mvvm.database.Column;",
                "import mobi.liason.mvvm.database.Model;",
                "import mobi.liason.mvvm.database.ModelColumn;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinition;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinitions;",
                "import mobi.liason.mvvm.database.annotations.PathDefinition;",
                "import mobi.liason.mvvm.database.annotations.PathDefinitions;",
                "import mobi.liason.test.ProductJson;",
                "public class ProductModel extends Model {",
                "   public static final String NAME = ProductModel.class.getSimpleName();",
                "   @Override",
                "   public String getName(final Context context) {",
                "       return NAME;",
                "   }",
                "   public static ContentValues getContentValues(final ProductJson productJson) {",
                "       final ContentValues contentValues = new ContentValues();",
                "       contentValues.put(Columns.ID.getName(), productJson.getId());",
                "       return contentValues;",
                "   }",
                "   @ColumnDefinitions",
                "   public static class Columns {",
                "       @ColumnDefinition",
                "       public static final ModelColumn ID = new ModelColumn(ProductModel.NAME, Product.ID, Column.Type.text);",
                "   }",
                "   @PathDefinitions",
                "   public static class Paths {",
                "       @PathDefinition",
                "       public static final Path PRODUCT = new Path(ProductModel.NAME);",
                "   }",
                "}"));

        ASSERT.about(javaSources())
                .that(asList(metaJavaFileObject))
                .processedWith(MODEL_PROCESSORS())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedJavaFileObject);
    }

    @Test
    public void createModelWithPrimaryKeyTextVariable() {
        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "package mobi.liason.test;",
                "import mobi.liason.annotation.Model;",
                "import mobi.liason.annotation.Text;",
                "import mobi.liason.mvvm.database.annotations.PrimaryKey;",
                "@Model",
                "public class Product {",
                "   @PrimaryKey",
                "   @Text",
                "   public static final String ID = \"id\";",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductModel", Joiner.on("\n").join(
                "package mobi.liason.test;",
                "import android.content.ContentValues;",
                "import android.content.Context;",
                "import mobi.liason.loaders.Path;",
                "import mobi.liason.mvvm.database.Column;",
                "import mobi.liason.mvvm.database.Model;",
                "import mobi.liason.mvvm.database.ModelColumn;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinition;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinitions;",
                "import mobi.liason.mvvm.database.annotations.PathDefinition;",
                "import mobi.liason.mvvm.database.annotations.PathDefinitions;",
                "import mobi.liason.mvvm.database.annotations.PrimaryKey;",
                "import mobi.liason.test.ProductJson;",
                "public class ProductModel extends Model {",
                "   public static final String NAME = ProductModel.class.getSimpleName();",
                "   @Override",
                "   public String getName(final Context context) {",
                "       return NAME;",
                "   }",
                "   public static ContentValues getContentValues(final ProductJson productJson) {",
                "       final ContentValues contentValues = new ContentValues();",
                "       contentValues.put(Columns.ID.getName(), productJson.getId());",
                "       return contentValues;",
                "   }",
                "   @ColumnDefinitions",
                "   public static class Columns {",
                "       @ColumnDefinition",
                "       @PrimaryKey",
                "       public static final ModelColumn ID = new ModelColumn(ProductModel.NAME, Product.ID, Column.Type.text);",
                "   }",
                "   @PathDefinitions",
                "   public static class Paths {",
                "       @PathDefinition",
                "       public static final Path PRODUCT = new Path(ProductModel.NAME);",
                "   }",
                "}"));

        ASSERT.about(javaSources())
                .that(asList(metaJavaFileObject))
                .processedWith(MODEL_PROCESSORS())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedJavaFileObject);
    }

    @Test
    public void createModelWithArrayVariable() {
        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "package mobi.liason.test;",
                "import mobi.liason.annotation.Model;",
                "import mobi.liason.annotation.Integer;",
                "import mobi.liason.annotation.Text;",
                "import mobi.liason.mvvm.database.annotations.PrimaryKey;",
                "@Model",
                "public class Product {",
                "   @PrimaryKey",
                "   @Integer",
                "   public static final String ID = \"id\";",
                "   @PrimaryKey",
                "   @Integer",
                "   public static final String ID2 = \"id2\";",
                "   @Text(isArray = true)",
                "   public static final String NAME = \"name\";",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductModel", Joiner.on("\n").join(
                "package mobi.liason.test;",
                "import android.content.ContentValues;",
                "import android.content.Context;",
                "import mobi.liason.loaders.Path;",
                "import mobi.liason.mvvm.database.Column;",
                "import mobi.liason.mvvm.database.Model;",
                "import mobi.liason.mvvm.database.ModelColumn;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinition;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinitions;",
                "import mobi.liason.mvvm.database.annotations.PathDefinition;",
                "import mobi.liason.mvvm.database.annotations.PathDefinitions;",
                "import mobi.liason.mvvm.database.annotations.PrimaryKey;",
                "import mobi.liason.test.ProductJson;",
                "public class ProductModel extends Model {",
                "   public static final String NAME = ProductModel.class.getSimpleName();",
                "   @Override",
                "   public String getName(final Context context) {",
                "       return NAME;",
                "   }",
                "   public static ContentValues getContentValues(final ProductJson productJson) {",
                "       final ContentValues contentValues = new ContentValues();",
                "       contentValues.put(Columns.ID.getName(), productJson.getId());",
                "       contentValues.put(Columns.ID2.getName(), productJson.getId2());",
                "       return contentValues;",
                "   }",
                "   @ColumnDefinitions",
                "   public static class Columns {",
                "       @ColumnDefinition",
                "       @PrimaryKey",
                "       public static final ModelColumn ID = new ModelColumn(ProductModel.NAME, Product.ID, Column.Type.integer);",
                "       @ColumnDefinition",
                "       @PrimaryKey",
                "       public static final ModelColumn ID2 = new ModelColumn(ProductModel.NAME, Product.ID2, Column.Type.integer);",
                "   }",
                "   @PathDefinitions",
                "   public static class Paths {",
                "       @PathDefinition",
                "       public static final Path PRODUCT = new Path(ProductModel.NAME);",
                "   }",
                "}"));

        final JavaFileObject expectedRelationModelJavaFileObject = JavaFileObjects.forSourceString("ProductNameRelationModel", Joiner.on("\n").join(
                "package mobi.liason.test;",
                "import android.content.ContentValues;",
                "import android.content.Context;",
                "import mobi.liason.loaders.Path;",
                "import mobi.liason.mvvm.database.Column;",
                "import mobi.liason.mvvm.database.ForeignKeyModelColumn;",
                "import mobi.liason.mvvm.database.Model;",
                "import mobi.liason.mvvm.database.ModelColumn;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinition;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinitions;",
                "import mobi.liason.mvvm.database.annotations.PathDefinition;",
                "import mobi.liason.mvvm.database.annotations.PathDefinitions;",
                "public class ProductNameRelationModel extends Model {",
                "   public static final String NAME = ProductNameRelationModel.class.getSimpleName();",
                "   @Override",
                "   public String getName(final Context context) {",
                "       return NAME;",
                "   }",
                "   public static ContentValues getContentValues(final Long productId, final Long productId2, final String name, final int index) {",
                "       final ContentValues contentValues = new ContentValues();",
                "       contentValues.put(Columns.PRODUCT_ID.getName(), productId);",
                "       contentValues.put(Columns.PRODUCT_ID2.getName(), productId2);",
                "       contentValues.put(Columns.NAME.getName(), name);",
                "       contentValues.put(Columns.INDEX.getName(), index);",
                "       return contentValues;",
                "   }",
                "   @ColumnDefinitions",
                "   public static class Columns {",
                "       @ColumnDefinition",
                "       public static final ForeignKeyModelColumn PRODUCT_ID = new ForeignKeyModelColumn(ProductNameRelationModel.NAME, ProductModel.Columns.ID);",
                "       @ColumnDefinition",
                "       public static final ForeignKeyModelColumn PRODUCT_ID2 = new ForeignKeyModelColumn(ProductNameRelationModel.NAME, ProductModel.Columns.ID2);",
                "       @ColumnDefinition",
                "       public static final ModelColumn NAME = new ModelColumn(ProductNameRelationModel.NAME, Product.NAME, Column.Type.text);",
                "       @ColumnDefinition",
                "       public static final ModelColumn INDEX = new ModelColumn(ProductNameRelationModel.NAME, \"index\", Column.Type.integer);",
                "   }",
                "   @PathDefinitions",
                "   public static class Paths {",
                "       @PathDefinition",
                "       public static final Path PRODUCT_NAME_RELATION_MODEL = new Path(ProductNameRelationModel.NAME);",
                "   }",
                "}"));

        ASSERT.about(javaSources())
                .that(asList(metaJavaFileObject))
                .processedWith(MODEL_PROCESSORS())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedJavaFileObject, expectedRelationModelJavaFileObject);
    }

}
