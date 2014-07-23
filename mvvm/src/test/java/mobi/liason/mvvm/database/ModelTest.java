package mobi.liason.mvvm.database;

import android.content.Context;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.Set;

import mobi.liason.loaders.Path;
import mobi.liason.mvvm.RobolectricTestRunnerWithInjection;
import mobi.liason.mvvm.database.annotations.ColumnDefinition;
import mobi.liason.mvvm.database.annotations.ColumnDefinitions;
import mobi.liason.mvvm.database.annotations.PrimaryKey;
import mobi.liason.mvvm.database.annotations.PathDefinition;
import mobi.liason.mvvm.database.annotations.PathDefinitions;
import mobi.liason.mvvm.database.annotations.Unique;


import static org.fest.assertions.api.Assertions.assertThat;

@Config(manifest = "/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunnerWithInjection.class)
public class ModelTest {

    public final Context mContext = Robolectric.getShadowApplication().getApplicationContext();

    @Test
    public void getCreateWithOneParameter_returnsCorrectSqlTableCreationAndDrop(){
        final MockModel mockModel = new MockModel("EMIR", new Path("PATH1", "PATH2"));
        final ModelColumn modelColumn = new ModelColumn("CONTENT_NAME", "COLUMN_NAME", Column.Type.text);
        mockModel.setModelColums(modelColumn);

        final String sqlCreateQuery = mockModel.getCreate(mContext);
        assertThat(sqlCreateQuery).isEqualTo("CREATE TABLE IF NOT EXISTS EMIR ( COLUMN_NAME TEXT );");

        final String sqlDropQuery = mockModel.getDrop(mContext);
        assertThat(sqlDropQuery).isEqualTo("DROP TABLE IF EXISTS EMIR;");
    }

    @Test
    public void getCreateWithOneParameterAndUnique_returnsCorrectSqlTableCreationAndDrop(){
        final MockModel mockModel = new MockModel("EMIR", new Path("PATH1", "PATH2"));
        final ModelColumn modelColumn = new ModelColumn("CONTENT_NAME", "COLUMN_NAME", Column.Type.text);
        mockModel.setModelColums(modelColumn);
        mockModel.setUniqueModelColums(modelColumn);

        final String sqlCreateQuery = mockModel.getCreate(mContext);
        assertThat(sqlCreateQuery).isEqualTo("CREATE TABLE IF NOT EXISTS EMIR ( COLUMN_NAME TEXT, UNIQUE ( COLUMN_NAME ) ON CONFLICT REPLACE );");

        final String sqlDropQuery = mockModel.getDrop(mContext);
        assertThat(sqlDropQuery).isEqualTo("DROP TABLE IF EXISTS EMIR;");
    }

    @Test
    public void getCreateWithMultipleParameters_returnsCorrectSqlTableCreationAndDrop(){
        final MockModel mockModel = new MockModel("EMIR", new Path("PATH1", "PATH2"));
        final ModelColumn modelColumn = new ModelColumn("CONTENT_NAME", "COLUMN_NAME", Column.Type.text);
        final ModelColumn modelColumn1 = new ModelColumn("CONTENT_NAME1", "COLUMN_NAME1", Column.Type.integer);
        mockModel.setModelColums(modelColumn, modelColumn1);

        final String sqlCreateQuery = mockModel.getCreate(mContext);
        assertThat(sqlCreateQuery).isEqualTo("CREATE TABLE IF NOT EXISTS EMIR ( COLUMN_NAME TEXT, COLUMN_NAME1 INTEGER );");

        final String sqlDropQuery = mockModel.getDrop(mContext);
        assertThat(sqlDropQuery).isEqualTo("DROP TABLE IF EXISTS EMIR;");
    }


    @Test
    public void createShouldUseUniquesPrimaryKeyAndColumns(){
        final MockAnnotationModel mockAnnotationModel = new MockAnnotationModel();
        final String sqlCreateQuery = mockAnnotationModel.getCreate(mContext);
        assertThat(sqlCreateQuery).isEqualTo("CREATE TABLE IF NOT EXISTS MockAnnotationModel " +
                "(" +
                    " MODEL_COLUMN_1 TEXT," +
                    " MODEL_COLUMN_2 TEXT," +
                    " MODEL_COLUMN_3 TEXT," +
                    " UNIQUE ( MODEL_COLUMN_2 ) ON CONFLICT REPLACE," +
                    " PRIMARY KEY ( MODEL_COLUMN_3 ) " +
                ");");
    }


    @Test
    public void annotationsShouldBuildGetColumnsAndGetUniquesAndPathsAndGetId(){
        final MockAnnotationModel mockAnnotationModel = new MockAnnotationModel();

        final List<Column> columns = mockAnnotationModel.getColumns(mContext);
        assertThat(columns).hasSize(3);

        final Set<Column> uniqueColumns = mockAnnotationModel.getUniqueColumns(mContext);
        assertThat(uniqueColumns).hasSize(1);

        final List<Path> paths = mockAnnotationModel.getPaths(mContext);
        assertThat(paths).hasSize(1);

    }

    public static class MockModel extends  Model {

        public String mName;
        public List<Path> mPathSegments;
        private List<Column> mModelColums;
        private Set<Column> mUniqueModelColums;

        public MockModel(String name, Path... pathSegments) {
            mName = name;
            mPathSegments = Lists.newArrayList(pathSegments);
        }

        public void setModelColums(Column... modelColums){
            mModelColums = Lists.newArrayList(modelColums);
        }

        public void setUniqueModelColums(Column... modelColums){
            mUniqueModelColums = Sets.newHashSet(modelColums);
        }

        @Override
        public String getName(Context context) {
            return mName;
        }

        @Override
        public List<Path> getPaths(Context context) {
            return mPathSegments;
        }

        @Override
        public List<Column> getColumns(Context context) {
            return mModelColums;
        }

        @Override
        public Set<Column> getUniqueColumns(Context context) {
            return mUniqueModelColums;
        }
    }

    public static class MockAnnotationModel extends  Model {

        public static final String NAME = MockAnnotationModel.class.getSimpleName();

        @Override
        public String getName(Context context) {
            return NAME;
        }

        @ColumnDefinitions
        public static class Columns {
            @ColumnDefinition
            public static final ModelColumn MODEL_COLUMN_1 = new ModelColumn(NAME, "MODEL_COLUMN_1", Column.Type.text);

            @Unique
            @ColumnDefinition
            public static final ModelColumn MODEL_COLUMN_2 = new ModelColumn(NAME, "MODEL_COLUMN_2", Column.Type.text);

            @PrimaryKey
            @ColumnDefinition
            public static final ModelColumn MODEL_COLUMN_3 = new ModelColumn(NAME, "MODEL_COLUMN_3", Column.Type.text);
        }

        @PathDefinitions
        public static class Paths {
            @PathDefinition
            public static final Path PATH = new Path(NAME);
        }

    }

}