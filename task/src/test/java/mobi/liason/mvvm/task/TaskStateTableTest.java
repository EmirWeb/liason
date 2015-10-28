package mobi.liason.mvvm.task;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import mobi.liason.loaders.Path;
import mobi.liason.mvvm.database.Column;

import static org.fest.assertions.api.Assertions.assertThat;

@Config(manifest = "/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class TaskStateTableTest {

    public Context mContext = RuntimeEnvironment.application.getBaseContext();


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