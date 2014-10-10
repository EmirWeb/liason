package mobi.liason.annotation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;

import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;

import static com.google.common.collect.Iterables.concat;
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
    public void ProcessesSimpleModel() {
        final JavaFileObject javaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "import mobi.liason.annotation.Model;",
                "@Model",
                "public class Product {",
                "}"));

        ASSERT.about(javaSources())
                .that(asList(javaFileObject))
                .processedWith(MODEL_PROCESSORS())
                .compilesWithoutError();
    }

    @Test
    public void CreatesEmptyJsonParser() {
        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "import mobi.liason.annotation.Model;",
                "@Model",
                "public class Product {",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductJson", Joiner.on("\n").join(
                "public class ProductJson {",
                "}"));

        ASSERT.about(javaSources())
                .that(asList(metaJavaFileObject))
                .processedWith(MODEL_PROCESSORS())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedJavaFileObject);
    }

    @Test
    public void CreatesEmptyJsonParserAndSamePackage() {
        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "package mobi.liason;",
                "import mobi.liason.annotation.Model;",
                "@Model",
                "public class Product {",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductJson", Joiner.on("\n").join(
                "package mobi.liason;",
                "public class ProductJson {",
                "}"));

        ASSERT.about(javaSources())
                .that(asList(metaJavaFileObject))
                .processedWith(MODEL_PROCESSORS())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedJavaFileObject);
    }

    @Test
    public void CreatesJsonModelWithIntegerMember() {
        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "import mobi.liason.annotation.Model;",
                "import mobi.liason.annotation.Integer;",
                "@Model",
                "public class Product {",
                "   @Integer",
                "   public static final String ID = \"id\";",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductJson", Joiner.on("\n").join(
                "import com.google.gson.annotations.SerializedName;",
                "public class ProductJson {",
                "   @SerializedName(Product.ID)\n" +
                "   private final Long mId;",
                "   public Product(final Long id) {",
                "       mId = id;",
                "   }",
                "   public Long getId() {",
                "       return mId;",
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
    public void CreatesJsonModelWithMultipleIntegerMembers() {
        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "import mobi.liason.annotation.Model;",
                "import mobi.liason.annotation.Integer;",
                "@Model",
                "public class Product {",
                "   @Integer",
                "   public static final String ID = \"id\";",
                "   @Integer",
                "   public static final String COUNT = \"count\";",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductJson", Joiner.on("\n").join(
                "import com.google.gson.annotations.SerializedName;",
                "public class ProductJson {",
                "   @SerializedName(Product.ID)\n" +
                "   private final Long mId;",
                "   @SerializedName(Product.COUNT)\n" +
                "   private final Long mCount;",
                "   public Product(final Long id, final Long count) {",
                "       mId = id;",
                "       mCount = count;",
                "   }",
                "   public Long getId() {",
                "       return mId;",
                "   }",
                "   public Long getCount() {",
                "       return mCount;",
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
