package mobi.liason.mvvm.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ViewModelColumnTest {

    @Test
    public void getColumnLineWithNoTable_returnsSameAsColumn(){
        final ViewModelColumn viewModelColumn = new ViewModelColumn("NAME", "COLUMN_NAME", Column.Type.text);
        final Column column = new Column("NAME", "COLUMN_NAME", Column.Type.text);
        final String viewModelColumnLine = viewModelColumn.getColumnLine();
        final String columnLine = column.getColumnLine();
        assertThat(viewModelColumnLine).isEqualTo(columnLine);
    }

    @Test
    public void getColumnLineWithNoTableAndCustomType_returnsSameAsColumn(){
        final ViewModelColumn viewModelColumn = new ViewModelColumn("NAME", "COLUMN_NAME", Column.Type.text, "VARCHAR(256)");
        final Column column = new Column("NAME", "COLUMN_NAME", Column.Type.text, "VARCHAR(256)");
        final String viewModelColumnLine = viewModelColumn.getColumnLine();
        final String columnLine = column.getColumnLine();
        assertThat(viewModelColumnLine).isEqualTo(columnLine);
    }

    @Test
    public void getColumnLineWithModelColumn_returnsCorrectly(){
        final ModelColumn modelColumn = new ModelColumn("MODEL_NAME", "MODEL_COLUMN_NAME", Column.Type.text);
        final ViewModelColumn viewModelColumn = new ViewModelColumn("NAME", modelColumn);
        final String viewModelColumnLine = viewModelColumn.getColumnLine();

        assertThat(viewModelColumnLine).isEqualTo(" MODEL_NAME.MODEL_COLUMN_NAME AS MODEL_COLUMN_NAME ");
    }

    @Test
    public void getColumnLineWithModelColumnAndRename_returnsCorrectly(){
        final ModelColumn modelColumn = new ModelColumn("MODEL_NAME", "MODEL_COLUMN_NAME", Column.Type.text);
        final ViewModelColumn viewModelColumn = new ViewModelColumn("NAME", "VIEW_MODEL_COLUMN", modelColumn);
        final String viewModelColumnLine = viewModelColumn.getColumnLine();

        assertThat(viewModelColumnLine).isEqualTo(" MODEL_NAME.MODEL_COLUMN_NAME AS VIEW_MODEL_COLUMN ");
    }


}