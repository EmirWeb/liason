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

@RunWith(JUnit4.class)
public class ModelProcessorTest {
    static final Iterable<? extends Processor> MODEL_PROCESSORS(){
        return Collections.singletonList(new ModelProcessor());
    }

    @Test
    public void modelAnnotationCreatesJsonAndModel (){
        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "package mobi.liason.test;",
                "import mobi.liason.annotation.Model;",
                "import mobi.liason.annotation.Object;",
                "@Model",
                "public class Product {",
                "   @Object(\"mobi.liason.ImagesJson\")",
                "   public static final String ID = \"id\";",
                "}"));

        final JavaFileObject imageMetaJavaFileObject = JavaFileObjects.forSourceString("Images", Joiner.on("\n").join(
                "package mobi.liason;",
                "import mobi.liason.annotation.Model;",
                "import mobi.liason.annotation.Text;",
                "@Model",
                "public class Images {",
                "   @Text",
                "   public static final String URL = \"url\";",
                "}"));


        final JavaFileObject expectedModelJavaFileObject = JavaFileObjects.forSourceString("ProductModel", Joiner.on("\n").join(
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
                "       return contentValues;",
                "   }",
                "   @ColumnDefinitions",
                "   public static class Columns {",
                "   }",
                "   @PathDefinitions",
                "   public static class Paths {",
                "       @PathDefinition",
                "       public static final Path PRODUCT = new Path(ProductModel.NAME);",
                "   }",
                "}"));
        final JavaFileObject expectedJsonJavaFileObject = JavaFileObjects.forSourceString("ProductJson", Joiner.on("\n").join(
                "package mobi.liason.test;",
                "import com.google.gson.annotations.SerializedName;",
                "import mobi.liason.ImagesJson;",
                "public class ProductJson {",
                "   @SerializedName(Product.ID)\n" +
                        "   private final ImagesJson mId;",
                "   public Product(final ImagesJson id) {",
                "       mId = id;",
                "   }",
                "   public ImagesJson getId() {",
                "       return mId;",
                "   }",
                "}"));

        ASSERT.about(javaSources())
                .that(asList(imageMetaJavaFileObject, metaJavaFileObject))
                .processedWith(MODEL_PROCESSORS())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedModelJavaFileObject, expectedJsonJavaFileObject);
    }

    @Test
    public void jsonAnnotationCreatesOnlyJson(){
        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "package mobi.liason.test;",
                "import mobi.liason.annotation.Json;",
                "import mobi.liason.annotation.Text;",
                "@Json",
                "public class Product {",
                "   @Text",
                "   public static final String ID = \"id\";",
                "}"));
        final JavaFileObject expectedJsonJavaFileObject = JavaFileObjects.forSourceString("ProductJson", Joiner.on("\n").join(
                "package mobi.liason.test;",
                "import com.google.gson.annotations.SerializedName;",
                "public class ProductJson {",
                "   @SerializedName(Product.ID)\n" +
                        "   private final String mId;",
                "   public Product(final String id) {",
                "       mId = id;",
                "   }",
                "   public String getId() {",
                "       return mId;",
                "   }",
                "}"));

        ASSERT.about(javaSources())
                .that(asList(metaJavaFileObject))
                .processedWith(MODEL_PROCESSORS())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedJsonJavaFileObject);
    }

}