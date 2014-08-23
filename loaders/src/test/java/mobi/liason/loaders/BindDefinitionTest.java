package mobi.liason.loaders;

import android.app.LoaderManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@Config(manifest = "/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunnerWithInjection.class)
public class BindDefinitionTest {

    public LoaderManager mLoaderManager;
    public Context mContext = Robolectric.getShadowApplication().getApplicationContext();
    private BindingManager mBindingManager;


    @Before
    public void setup() {
        mLoaderManager = mock(LoaderManager.class);
        mBindingManager = new BindingManager(mContext, mLoaderManager);
    }


    @Test
    public void differentIdsForDifferentChildrenOfItemBinding() {
        final Context context = Robolectric.getShadowApplication().getApplicationContext();
        final BindDefinition1 bindDefinition1 = new BindDefinition1(context);
        final BindDefinition2 bindDefinition2 = new BindDefinition2(context);
        final int id1 = bindDefinition1.getId(mContext);
        final int id2 = bindDefinition2.getId(mContext);
        assertThat(id1).isNotEqualTo(id2);
    }

    public class BindDefinition1 extends BindDefinition {

        public BindDefinition1(Context context) {
            super(context);
        }

        @Override
        public void onBind(Context context, Cursor cursor) {

        }

        @Override
        public Uri getUri(Context context) {
            return null;
        }

    }

    public class BindDefinition2 extends BindDefinition {

        public BindDefinition2(Context context) {
            super(context);
        }

        @Override
        public void onBind(Context context, Cursor cursor) {

        }

        @Override
        public Uri getUri(Context context) {
            return null;
        }
    }

}