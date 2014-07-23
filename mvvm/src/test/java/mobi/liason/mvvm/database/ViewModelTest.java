package mobi.liason.mvvm.database;

import android.content.Context;

import com.google.common.collect.Lists;

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
import mobi.liason.mvvm.database.annotations.Unique;


import static org.fest.assertions.api.Assertions.assertThat;

@Config(manifest = "/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunnerWithInjection.class)
public class ViewModelTest {

    private Context mContext = Robolectric.getShadowApplication().getApplicationContext();

    @Test
    public void getCreateWithOneViewModelColumn_returnsCorrectCreateAndDrop(){
        final MockViewModel mockViewModel = new MockViewModel("VIEW_MODEL", new Path("PATH1", "PATH2"));
        mockViewModel.setViewModelColums(new ViewModelColumn("VIEW_MODEL","VIEW_MODEL_COLUMN", Column.Type.blob));
        final String sqlCreateQuery = mockViewModel.getCreate(mContext);
        assertThat(sqlCreateQuery).isEqualTo("CREATE VIEW IF NOT EXISTS VIEW_MODEL AS SELECT VIEW_MODEL_COLUMN FROM TABLE JOIN MYTABLE;");
    }

    @Test
    public void getCreateWithMultipleViewModelColumn_returnsCorrectCreateAndDrop(){
        final MockViewModel mockViewModel = new MockViewModel("VIEW_MODEL", new Path("PATH1", "PATH2"));
        final ViewModelColumn viewModelColumn = new ViewModelColumn("VIEW_MODEL", "VIEW_MODEL_COLUMN", Column.Type.blob);
        final ViewModelColumn viewModelColumn1 = new ViewModelColumn("VIEW_MODEL1", "VIEW_MODEL_COLUMN1", Column.Type.real);
        mockViewModel.setViewModelColums(viewModelColumn, viewModelColumn1);
        final String sqlCreateQuery = mockViewModel.getCreate(mContext);
        assertThat(sqlCreateQuery).isEqualTo("CREATE VIEW IF NOT EXISTS VIEW_MODEL AS SELECT VIEW_MODEL_COLUMN, VIEW_MODEL_COLUMN1 FROM TABLE JOIN MYTABLE;");
    }

    @Test
    public void getCreateWithNoViewModelColumn_returnsSelectStar(){
        final MockViewModel mockViewModel = new MockViewModel("VIEW_MODEL", new Path("PATH1", "PATH2"));
        final String sqlCreateQuery = mockViewModel.getCreate(mContext);
        assertThat(sqlCreateQuery).isEqualTo("CREATE VIEW IF NOT EXISTS VIEW_MODEL AS SELECT * FROM TABLE JOIN MYTABLE;");
    }

    @Test
    public void annotationsShouldBuildGetColumnsAndGetUniquesAndGetId(){
        final MockAnnotationsViewModel mockAnnotationsViewModel = new MockAnnotationsViewModel(new Path("PATH1", "PATH2"));

        final List<Column> columns = mockAnnotationsViewModel.getColumns(mContext);
        assertThat(columns).hasSize(3);

        final Set<Column> uniqueColumns = mockAnnotationsViewModel.getUniqueColumns(mContext);
        assertThat(uniqueColumns).hasSize(2);

    }

    public static class MockViewModel extends ViewModel {

        public String mName;
        public List<Path> mPathSegments;
        private List<Column> mModelColums;

        public MockViewModel(String name, Path... pathSegments) {
            mName = name;
            mPathSegments = Lists.newArrayList(pathSegments);
        }

        public void setViewModelColums(Column... modelColums) {
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

        @Override
        protected String getSelection(Context context) {
            return "TABLE JOIN MYTABLE";
        }
    }

    public static class MockAnnotationsViewModel extends ViewModel {

        public static final String NAME = MockAnnotationsViewModel.class.getSimpleName();
        public List<Path> mPathSegments;

        public MockAnnotationsViewModel(Path... pathSegments) {
            mPathSegments = Lists.newArrayList(pathSegments);
        }

        @Override
        public String getName(Context context) {
            return NAME;
        }

        @Override
        public List<Path> getPaths(Context context) {
            return mPathSegments;
        }


        @Override
        protected String getSelection(Context context) {
            return "TABLE JOIN MYTABLE";
        }

        @ColumnDefinitions
        public static class Columns {
            @ColumnDefinition
            public static final ModelColumn MODEL_COLUMN_1 = new ModelColumn(NAME, "MODEL_COLUMN_1", Column.Type.text);

            @Unique
            @ColumnDefinition
            public static final ModelColumn MODEL_COLUMN_2 = new ModelColumn(NAME, "MODEL_COLUMN_2", Column.Type.text);

            @PrimaryKey
            @Unique
            @ColumnDefinition
            public static final ModelColumn MODEL_COLUMN_3 = new ModelColumn(NAME, "MODEL_COLUMN_3", Column.Type.text);
        }
    }
}