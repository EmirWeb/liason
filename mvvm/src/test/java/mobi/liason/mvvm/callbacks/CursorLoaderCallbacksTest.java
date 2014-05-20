package mobi.liason.mvvm.callbacks;

import android.app.LoaderManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import mobi.liason.mvvm.RobolectricTestRunnerWithInjection;
import mobi.liason.mvvm.bindings.BindDefinition;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class CursorLoaderCallbacksTest {

    public LoaderManager mLoaderManager;
    public Context mContext = Robolectric.getShadowApplication().getApplicationContext();
    private CursorLoaderCallbacks mCursorLoaderCallbacks;

    @Before
    public void setup(){
        mLoaderManager = mock(LoaderManager.class);
        mCursorLoaderCallbacks = new CursorLoaderCallbacks(mContext, mLoaderManager);
    }

    @Test
    public void onStartCallsInitLoaderTwice(){
        mCursorLoaderCallbacks.addBindDefinition(new MockBindDefinition());
        mCursorLoaderCallbacks.addBindDefinition(new MockBindDefinition());
        mCursorLoaderCallbacks.onStart(mContext);
        verify(mLoaderManager, atLeast(2)).initLoader(anyInt(), any(Bundle.class), any(LoaderManager.LoaderCallbacks.class));
    }


    @Test
    public void addingDefinitionAfterStartedCallsInitLoaderTwice(){
        mCursorLoaderCallbacks.onStart(mContext);
        mCursorLoaderCallbacks.addBindDefinition(new MockBindDefinition());
        mCursorLoaderCallbacks.addBindDefinition(new MockBindDefinition());
        verify(mLoaderManager, atLeast(2)).initLoader(anyInt(), any(Bundle.class), any(LoaderManager.LoaderCallbacks.class));
    }

    @Test
    public void addingDefinitionBeforeStartedDoesNotCallInit(){
        mCursorLoaderCallbacks.addBindDefinition(new MockBindDefinition());
        mCursorLoaderCallbacks.addBindDefinition(new MockBindDefinition());
        verify(mLoaderManager, never()).initLoader(anyInt(), any(Bundle.class), any(LoaderManager.LoaderCallbacks.class));
    }

    @Test
    public void onStartDoesNotCallInitLoader(){
        mCursorLoaderCallbacks.onStart(mContext);
        verify(mLoaderManager, never()).initLoader(anyInt(), any(Bundle.class), any(LoaderManager.LoaderCallbacks.class));
    }

    public static class MockBindDefinition extends BindDefinition {

        @Override
        public void onBind(Cursor cursor) {

        }

        @Override
        public Uri getUri() {
            return Uri.parse("http://parchment.mobi");
        }
    }

}