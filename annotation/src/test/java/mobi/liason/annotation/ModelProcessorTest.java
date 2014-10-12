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
public class ModelProcessorTest {
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
                "import mobi.liason.mvvm.database.Model;",
                "public class ProductModel extends Model {",
                "   public static final String NAME = ProductModel.class.getSimpleName();",
                "   @Override",
                "   public String getName(final Context context) {",
                "       return NAME;",
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
    public void CreatesJsonModelWithTextArrayMember() {
        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "import mobi.liason.annotation.Model;",
                "import mobi.liason.annotation.Text;",
                "@Model",
                "public class Product {",
                "   @Text",
                "   public static final String ID = \"id\";",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductModel", Joiner.on("\n").join(
                "import android.content.Context;",
                "import mobi.liason.mvvm.database.Model;",
                "import android.content.ContentValues;",
                "import mobi.liason.mvvm.database.Column;",
                "import mobi.liason.mvvm.database.ModelColumn;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinition;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinitions;",
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
                "}"));

        ASSERT.about(javaSources())
                .that(asList(metaJavaFileObject))
                .processedWith(MODEL_PROCESSORS())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedJavaFileObject);
    }

}
