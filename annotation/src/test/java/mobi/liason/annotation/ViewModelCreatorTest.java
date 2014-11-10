package mobi.liason.annotation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;

import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;

import mobi.liason.loaders.*;
import mobi.liason.loaders.Path;
import mobi.liason.mvvm.database.ViewModelColumn;

import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;
import static java.util.Arrays.asList;
import static org.truth0.Truth.ASSERT;

/**
 * Created by Emir Hasanbegovic on 09/10/14.
 */
@RunWith(JUnit4.class)
public class ViewModelCreatorTest {
    static final Iterable<? extends Processor> MODEL_PROCESSORS(){
        return Collections.singletonList(new ViewModelProcessor());
    }


    @Test
    public void CreatesViewModelWithSelection() {

        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "import mobi.liason.annotation.annotations.mvvm.ViewModel;",
                "import mobi.liason.annotation.annotations.Selection;",
                "@ViewModel",
                "public class Product {",
                "   @Selection",
                "   public static final String SELECTION = \"Hello\";",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductViewModel", Joiner.on("\n").join(
                "import android.content.Context;",
                "import mobi.liason.loaders.Path;",
                "import mobi.liason.mvvm.database.ViewModel;",
                "import mobi.liason.mvvm.database.annotations.PathDefinition;",
                "import mobi.liason.mvvm.database.annotations.PathDefinitions;",
                "public class ProductViewModel extends ViewModel {",
                "   public static final String NAME = ProductViewModel.class.getSimpleName();",
                "   @Override",
                "   public String getName(final Context context) {",
                "       return NAME;",
                "   }",
                "   @Override",
                "   protected String getSelection(final Context context) {",
                "       return Product.SELECTION;",
                "   }",
                "   @PathDefinitions",
                "   public static class Paths {",
                "       @PathDefinition",
                "       public static final Path PRODUCT = new Path(ProductViewModel.NAME);",
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
    public void CreatesViewModelWithProjection() {

        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "import mobi.liason.annotation.annotations.mvvm.ViewModel;",
                "import mobi.liason.annotation.annotations.Selection;",
                "import mobi.liason.annotation.annotations.Projection;",
                "import mobi.liason.mvvm.database.ViewModelColumn;",
                "import mobi.liason.mvvm.database.Column.Type;",
                "import android.provider.BaseColumns;",
                "@ViewModel",
                "public class Product {",
                "   @Projection",
                "   public static final ViewModelColumn _ID = new ViewModelColumn(ProductViewModel.NAME, \"columnName\", Type.integer);",
                "   @Selection",
                "   public static final String SELECTION = \"Hello\";",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductViewModel", Joiner.on("\n").join(
                "import android.content.Context;",
                "import mobi.liason.loaders.Path;",
                "import mobi.liason.mvvm.database.ViewModel;",
                "import mobi.liason.mvvm.database.ViewModelColumn;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinition;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinitions;",
                "import mobi.liason.mvvm.database.annotations.PathDefinition;",
                "import mobi.liason.mvvm.database.annotations.PathDefinitions;",
                "public class ProductViewModel extends ViewModel {",
                "   public static final String NAME = ProductViewModel.class.getSimpleName();",
                "   @Override",
                "   public String getName(final Context context) {",
                "       return NAME;",
                "   }",
                "   @Override",
                "   protected String getSelection(final Context context) {",
                "       return Product.SELECTION;",
                "   }",
                "   @ColumnDefinitions",
                "   public static class Columns {",
                "       @ColumnDefinition",
                "       public static final ViewModelColumn _ID = Product._ID;",
                "   }",
                "   @PathDefinitions",
                "   public static class Paths {",
                "       @PathDefinition",
                "       public static final Path PRODUCT = new Path(ProductViewModel.NAME);",
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
    public void CreatesViewModelWithCustomPaths() {

        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "import mobi.liason.annotation.annotations.mvvm.ViewModel;",
                "import mobi.liason.annotation.annotations.Selection;",
                "import mobi.liason.annotation.annotations.Path;",
                "import mobi.liason.annotation.annotations.Projection;",
                "import mobi.liason.mvvm.database.ViewModelColumn;",
                "import mobi.liason.mvvm.database.Column.Type;",
                "import android.provider.BaseColumns;",
                "@ViewModel",
                "public class Product {",
                "   @Projection",
                "   public static final ViewModelColumn _ID = new ViewModelColumn(ProductViewModel.NAME, \"columnName\", Type.integer);",
                "   @Selection",
                "   public static final String SELECTION = \"Hello\";",
                "   @Path",
                "   public static final String MAIN_ROUTE = \"PATH1/Path2/#\";",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductViewModel", Joiner.on("\n").join(
                "import android.content.Context;",
                "import mobi.liason.loaders.Path;",
                "import mobi.liason.mvvm.database.ViewModel;",
                "import mobi.liason.mvvm.database.ViewModelColumn;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinition;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinitions;",
                "import mobi.liason.mvvm.database.annotations.PathDefinition;",
                "import mobi.liason.mvvm.database.annotations.PathDefinitions;",
                "public class ProductViewModel extends ViewModel {",
                "   public static final String NAME = ProductViewModel.class.getSimpleName();",
                "   @Override",
                "   public String getName(final Context context) {",
                "       return NAME;",
                "   }",
                "   @Override",
                "   protected String getSelection(final Context context) {",
                "       return Product.SELECTION;",
                "   }",
                "   @ColumnDefinitions",
                "   public static class Columns {",
                "       @ColumnDefinition",
                "       public static final ViewModelColumn _ID = Product._ID;",
                "   }",
                "   @PathDefinitions",
                "   public static class Paths {",
                "       @PathDefinition",
                "       public static final Path PRODUCT = new Path(ProductViewModel.NAME);",
                "       @PathDefinition",
                "       public static final Path MAIN_ROUTE = new Path(Product.MAIN_ROUTE);",
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
    public void CreatesViewModelWithPathAction() {

        final JavaFileObject metaJavaFileObject = JavaFileObjects.forSourceString("Product", Joiner.on("\n").join(
                "import android.content.Context;",
                "import android.database.Cursor;",
                "import android.database.sqlite.SQLiteDatabase;",
                "import android.net.Uri;",
                "import mobi.liason.annotation.annotations.mvvm.ViewModel;",
                "import mobi.liason.annotation.annotations.Selection;",
                "import mobi.liason.annotation.annotations.Path;",
                "import mobi.liason.annotation.annotations.PathAction;",
                "import mobi.liason.annotation.annotations.PathAction.PathType;",
                "import mobi.liason.annotation.annotations.Projection;",
                "import mobi.liason.mvvm.database.ViewModelColumn;",
                "import mobi.liason.mvvm.database.Column.Type;",
                "import android.provider.BaseColumns;",
                "@ViewModel",
                "public class Product {",
                "   @Projection",
                "   public static final ViewModelColumn _ID = new ViewModelColumn(ProductViewModel.NAME, \"columnName\", Type.integer);",
                "   @Selection",
                "   public static final String SELECTION = \"Hello\";",
                "   @Path",
                "   public static final String MAIN_ROUTE = \"PATH1/Path2/#\";",
                "   @PathAction(value = MAIN_ROUTE, pathType = PathType.query)",
                "   public static Cursor customQuery(final ProductViewModel productViewModel, final Context context, final SQLiteDatabase sqLiteDatabase, final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {",
                "       return null;",
                "   }",
                "}"));

        final JavaFileObject expectedJavaFileObject = JavaFileObjects.forSourceString("ProductViewModel", Joiner.on("\n").join(
                "import android.content.Context;",
                "import android.database.Cursor;",
                "import android.database.sqlite.SQLiteDatabase;",
                "import android.net.Uri;",
                "import mobi.liason.loaders.Path;",
                "import mobi.liason.mvvm.database.ViewModel;",
                "import mobi.liason.mvvm.database.ViewModelColumn;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinition;",
                "import mobi.liason.mvvm.database.annotations.ColumnDefinitions;",
                "import mobi.liason.mvvm.database.annotations.PathDefinition;",
                "import mobi.liason.mvvm.database.annotations.PathDefinitions;",
                "public class ProductViewModel extends ViewModel {",
                "   public static final String NAME = ProductViewModel.class.getSimpleName();",
                "   @Override",
                "   public String getName(final Context context) {",
                "       return NAME;",
                "   }",
                "   @Override",
                "   protected String getSelection(final Context context) {",
                "       return Product.SELECTION;",
                "   }",
                "   @ColumnDefinitions",
                "   public static class Columns {",
                "       @ColumnDefinition",
                "       public static final ViewModelColumn _ID = Product._ID;",
                "   }",
                "   @PathDefinitions",
                "   public static class Paths {",
                "       @PathDefinition",
                "       public static final Path PRODUCT = new Path(ProductViewModel.NAME);",
                "       @PathDefinition",
                "       public static final Path MAIN_ROUTE = new Path(Product.MAIN_ROUTE);",
                "   }",
                "   @Override",
                "   public Cursor query(final Context context, final SQLiteDatabase sQLiteDatabase, final Path path, final Uri uri, final String[] projection, final String selection, final String[] selectionArguments, final String sortOrder) {",
                "       if (path != null && path.equals(new Path(\"PATH1/Path2/#\"))) {",
                "           return Product.customQuery(this, context, sQLiteDatabase, uri, projection, selection, selectionArguments, sortOrder);",
                "       }",
                "       return super.query(context, sQLiteDatabase, path, uri, projection, selection, selectionArguments, sortOrder);",
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
