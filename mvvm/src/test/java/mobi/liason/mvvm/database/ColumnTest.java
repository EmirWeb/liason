package mobi.liason.mvvm.database;

import android.database.Cursor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    public void getValueReturnsNull(){
        final Column column = new Column("NAME", "COLUMN_NAME", Column.Type.text);
        Cursor mockCursor = mock(Cursor.class);
        when(mockCursor.getCount()).thenReturn(1);
        when(mockCursor.getColumnIndex(anyString())).thenReturn(0);
        when(mockCursor.isNull(anyInt())).thenReturn(true);
        final Object value = column.getValue(mockCursor);
        assertThat(value).isNull();
    }
}