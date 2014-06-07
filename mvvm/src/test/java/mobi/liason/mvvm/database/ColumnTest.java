package mobi.liason.mvvm.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.fest.assertions.api.Assertions.assertThat;

@Config(manifest = "/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ColumnTest {

    @Test
    public void getColumnLine_WithCustomType(){
        final Column column = new Column("NAME", "COLUMN_NAME", Column.Type.text, "VARCHAR(256)");
        final String columnLine = column.getColumnLine();
        assertThat(columnLine).isEqualTo("COLUMN_NAME VARCHAR(256)");
    }

    @Test
    public void getColumnLine_withDefaultType(){
        final Column column = new Column("NAME", "COLUMN_NAME", Column.Type.text);
        final String columnLine = column.getColumnLine();
        assertThat(columnLine).isEqualTo("COLUMN_NAME TEXT");
    }
}