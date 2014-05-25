package mobi.liason.loaders;

import android.app.LoaderManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class BindingManagerTest {

    public LoaderManager mLoaderManager;
    public Context mContext = Robolectric.getShadowApplication().getApplicationContext();
    private BindingManager mBindingManager;

    @Before
    public void setup() {
        mLoaderManager = mock(LoaderManager.class);
        mBindingManager = new BindingManager(mContext, mLoaderManager);
    }

    @Test
    public void onStartCallsInitLoaderTwice() {
        mBindingManager.addBindDefinition(new MockBindDefinition(mContext));
        mBindingManager.addBindDefinition(new MockBindDefinition(mContext){
            @Override
            public int getId(Context context) {
                return 2;
            }
        });
        mBindingManager.onStart(mContext);
        verify(mLoaderManager, atLeast(2)).initLoader(anyInt(), any(Bundle.class), any(LoaderManager.LoaderCallbacks.class));
    }


    @Test
    public void addingDefinitionAfterStartedCallsInitLoaderTwice() {
        mBindingManager.onStart(mContext);
        mBindingManager.addBindDefinition(new MockBindDefinition(mContext));
        mBindingManager.addBindDefinition(new MockBindDefinition(mContext));
        verify(mLoaderManager, atLeast(2)).initLoader(anyInt(), any(Bundle.class), any(LoaderManager.LoaderCallbacks.class));
    }

    @Test
    public void addingDefinitionBeforeStartedDoesNotCallInit() {
        mBindingManager.addBindDefinition(new MockBindDefinition(mContext));
        mBindingManager.addBindDefinition(new MockBindDefinition(mContext));
        verify(mLoaderManager, never()).initLoader(anyInt(), any(Bundle.class), any(LoaderManager.LoaderCallbacks.class));
    }

    @Test
    public void onStartDoesNotCallInitLoader() {
        mBindingManager.onStart(mContext);
        verify(mLoaderManager, never()).initLoader(anyInt(), any(Bundle.class), any(LoaderManager.LoaderCallbacks.class));
    }

    public static class MockBindDefinition extends BindDefinition {

        public MockBindDefinition(Context context) {
            super(context);
        }

        @Override
        public void onBind(Context context, Cursor cursor) {

        }

        @Override
        public Uri getUri(Context context) {
            return Uri.parse("http://parchment.mobi");
        }

        @Override
        public int getId(Context context) {
            return 1;
        }
    }

}