package mobi.liason.mvvm.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ViewModelColumnTest {

    @Test
    public void getColumnLineWithNoTable_returnsJustTheColumnName(){
        final ViewModelColumn viewModelColumn = new ViewModelColumn("NAME", "COLUMN_NAME", Column.Type.text);
        final String viewModelColumnLine = viewModelColumn.getColumnLine();
        assertThat(viewModelColumnLine).isEqualTo("COLUMN_NAME");
    }

    @Test
    public void getColumnLineWithNoTableAndCustomType_returnsJustTheColumnName(){
        final ViewModelColumn viewModelColumn = new ViewModelColumn("NAME", "COLUMN_NAME", Column.Type.text, "VARCHAR(256)");
        final String viewModelColumnLine = viewModelColumn.getColumnLine();

        assertThat(viewModelColumnLine).isEqualTo("COLUMN_NAME");
    }

    @Test
    public void getColumnLineWithModelColumn_returnsCorrectly(){
        final ModelColumn modelColumn = new ModelColumn("MODEL_NAME", "MODEL_COLUMN_NAME", Column.Type.text);
        final ViewModelColumn viewModelColumn = new ViewModelColumn("NAME", modelColumn);
        final String viewModelColumnLine = viewModelColumn.getColumnLine();

        assertThat(viewModelColumnLine).isEqualTo("MODEL_NAME.MODEL_COLUMN_NAME AS MODEL_COLUMN_NAME");
    }

    @Test
    public void getColumnLineWithModelColumnAndRename_returnsCorrectly(){
        final ModelColumn modelColumn = new ModelColumn("MODEL_NAME", "MODEL_COLUMN_NAME", Column.Type.text);
        final ViewModelColumn viewModelColumn = new ViewModelColumn("NAME", "VIEW_MODEL_COLUMN", modelColumn);
        final String viewModelColumnLine = viewModelColumn.getColumnLine();

        assertThat(viewModelColumnLine).isEqualTo("MODEL_NAME.MODEL_COLUMN_NAME AS VIEW_MODEL_COLUMN");
    }

    @Test
    public void getColumnLineWithModelColumnRenameAndCustomSelection_returnsCorrectly(){
        final ViewModelColumn viewModelColumn = new ViewModelColumn("NAME", "VIEW_MODEL_COLUMN", "SELECT * FROM TABLE", Column.Type.blob);
        final String viewModelColumnLine = viewModelColumn.getColumnLine();

        assertThat(viewModelColumnLine).isEqualTo("SELECT * FROM TABLE AS VIEW_MODEL_COLUMN");
    }


}