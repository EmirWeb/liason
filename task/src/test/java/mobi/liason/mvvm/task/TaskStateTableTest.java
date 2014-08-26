package mobi.liason.mvvm.task;

import android.app.LoaderManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import mobi.liason.loaders.Path;
import mobi.liason.mvvm.database.Column;
import mobi.liason.mvvm.task.TaskStateTable;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@Config(manifest = "/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class TaskStateTableTest {

    public Context mContext = Robolectric.getShadowApplication().getApplicationContext();


    @Test
    public void columnCountTest (){
        final TaskStateTable taskStateTable = new TaskStateTable();
        final List<Column> columns = taskStateTable.getColumns(mContext);
        assertThat(columns).hasSize(4);
    }

    @Test
    public void pathCountTest(){
        final TaskStateTable taskStateTable = new TaskStateTable();
        final List<Path> paths = taskStateTable.getPaths(mContext);
        assertThat(paths).hasSize(1);
    }

}