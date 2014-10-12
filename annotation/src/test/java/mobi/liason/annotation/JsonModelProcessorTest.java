package mobi.liason.annotation;

import com.google.common.base.Joiner;
import com.google.testing.compile.CompileTester;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.truth0.AbstractVerb;

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
public class JsonModelProcessorTest {
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
                "   @SerializedName(Product.ID)",
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
                "   @SerializedName(Product.COUNT)",
                "   private final Long mCount;",
                "   @SerializedName(Product.ID)",
                "   private final Long mId;",
                "   public Product(final Long count, final Long id) {",
                "       mCount = count;",
                "       mId = id;",
                "   }",
                "   public Long getCount() {",
                "       return mCount;",
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
    public void CreatesJsonModelWithMultipleMembers() {
        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "import mobi.liason.annotation.Model;",
                "import mobi.liason.annotation.Integer;",
                "import mobi.liason.annotation.Text;",
                "@Model",
                "public class Product {",
                "   @Integer",
                "   public static final String ID = \"id\";",
                "   @Text",
                "   public static final String COUNT = \"count\";",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductJson", Joiner.on("\n").join(
                "import com.google.gson.annotations.SerializedName;",
                "public class ProductJson {",
                "   @SerializedName(Product.COUNT)",
                "   private final String mCount;",
                "   @SerializedName(Product.ID)",
                "   private final Long mId;",
                "   public Product(final String count, final Long id) {",
                "       mCount = count;",
                "       mId = id;",
                "   }",
                "   public String getCount() {",
                "       return mCount;",
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
    public void CreatesJsonModelWithTextMember() {
        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "import mobi.liason.annotation.Model;",
                "import mobi.liason.annotation.Text;",
                "@Model",
                "public class Product {",
                "   @Text",
                "   public static final String ID = \"id\";",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductJson", Joiner.on("\n").join(
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
                .generatesSources(expectedJavaFileObject);
    }

    @Test
    public void CreatesJsonModelWithRealMember() {
        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "import mobi.liason.annotation.Model;",
                "import mobi.liason.annotation.Real;",
                "@Model",
                "public class Product {",
                "   @Real",
                "   public static final String ID = \"id\";",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductJson", Joiner.on("\n").join(
                "import com.google.gson.annotations.SerializedName;",
                "public class ProductJson {",
                "   @SerializedName(Product.ID)",
                "   private final Double mId;",
                "   public Product(final Double id) {",
                "       mId = id;",
                "   }",
                "   public Double getId() {",
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
    public void CreatesJsonModelWithObjectMember() {

        final JavaFileObject imageMetaJavaFileObject = JavaFileObjects.forSourceString("Images", Joiner.on("\n").join(
                "package mobi.liason;",
                "import mobi.liason.annotation.Model;",
                "import mobi.liason.annotation.Text;",
                "@Model",
                "public class Images {",
                "   @Text",
                "   public static final String URL = \"url\";",
                "}"));

        final JavaFileObject expectedImageJavaFileObject = JavaFileObjects.forSourceString("ImagesJson", Joiner.on("\n").join(
                "package mobi.liason;",
                "import com.google.gson.annotations.SerializedName;",
                "public class ImagesJson {",
                "   @SerializedName(Images.URL)",
                "   private final String mUrl;",
                "   public ImagesJson(final String url) {",
                "       mUrl = url;",
                "   }",
                "   public String getUrl() {",
                "       return mUrl;",
                "   }",
                "}"));


        final JavaFileObject productMetaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "package mobi.liason;",
                "import mobi.liason.annotation.Model;",
                "import mobi.liason.annotation.Object;",
                "import mobi.liason.ImagesJson;",
                "@Model",
                "public class Product {",
                "   @Object(String.class)",
                "   public static final String IMAGES = \"images\";",
                "}"));

        final JavaFileObject expectedProductJavaFileObject = JavaFileObjects.forSourceString("ProductJson", Joiner.on("\n").join(
                "package mobi.liason;",
                "import com.google.gson.annotations.SerializedName;",
                "import java.lang.String;",
                "public class ProductJson {",
                "   @SerializedName(Product.IMAGES)",
                "   private final String mImages;",
                "   public Product(final String images) {",
                "       mImages = images;",
                "   }",
                "   public String getImages() {",
                "       return mImages;",
                "   }",
                "}"));

        final AbstractVerb.DelegatedVerb<JavaSourcesSubject, Iterable<? extends JavaFileObject>> about = ASSERT.about(javaSources());
        final JavaSourcesSubject that = about.that(asList(imageMetaJavaFileObject, productMetaJavaFileObject));
        final CompileTester compileTester = that.processedWith(MODEL_PROCESSORS());
        final CompileTester.SuccessfulCompilationClause successfulCompilationClause = compileTester.compilesWithoutError();
        final CompileTester.GeneratedPredicateClause and = successfulCompilationClause.and();
        and.generatesSources(expectedProductJavaFileObject);
    }

    @Test
    public void CreatesJsonModelWithTextArrayMember() {
        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "import mobi.liason.annotation.Model;",
                "import mobi.liason.annotation.Text;",
                "@Model",
                "public class Product {",
                "   @Text(isArray = true)",
                "   public static final String ID = \"id\";",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductJson", Joiner.on("\n").join(
                "import com.google.gson.annotations.SerializedName;",
                "import java.util.ArrayList;",
                "public class ProductJson {",
                "   @SerializedName(Product.ID)\n" +
                        "   private final ArrayList<String> mId;",
                "   public Product(final ArrayList<String> id) {",
                "       mId = id;",
                "   }",
                "   public ArrayList<String> getId() {",
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
}
