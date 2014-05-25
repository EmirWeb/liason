package mobi.liason.mvvm.database;

import android.content.Context;

import com.google.common.collect.Lists;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import java.util.List;

import mobi.liason.loaders.Path;
import mobi.liason.mvvm.RobolectricTestRunnerWithInjection;


import static org.fest.assertions.api.Assertions.assertThat;

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

    public static class MockModel extends  Model {

        public String mName;
        public List<Path> mPathSegments;
        private List<Column> mModelColums;

        public MockModel(String name, Path... pathSegments) {
            mName = name;
            mPathSegments = Lists.newArrayList(pathSegments);
        }

        public void setModelColums(Column... modelColums){
            mModelColums = Lists.newArrayList(modelColums);
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
    }

}