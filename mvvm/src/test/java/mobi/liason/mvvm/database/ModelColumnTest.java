package mobi.liason.mvvm.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.fest.assertions.api.Assertions.assertThat;

@Config(manifest = "/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ModelColumnTest {
    @Test
    public void getColumnLineWithNoTable_returnsSameAsColumn(){
        final ModelColumn modelColumn = new ModelColumn("NAME", "COLUMN_NAME", Column.Type.text);
        final Column column = new Column("NAME", "COLUMN_NAME", Column.Type.text);
        final String modelColumnLine = modelColumn.getColumnLine();
        final String columnLine = column.getColumnLine();
        assertThat(modelColumnLine).isEqualTo(columnLine);
    }

    @Test
    public void getColumnLineWithNoTableAndCustomType_returnsSameAsColumn(){
        final ModelColumn modelColumn = new ModelColumn("NAME", "COLUMN_NAME", Column.Type.text, "VARCHAR(256)");
        final Column column = new Column("NAME", "COLUMN_NAME", Column.Type.text, "VARCHAR(256)");
        final String modelColumnLine = modelColumn.getColumnLine();
        final String columnLine = column.getColumnLine();
        assertThat(modelColumnLine).isEqualTo(columnLine);
    }
}