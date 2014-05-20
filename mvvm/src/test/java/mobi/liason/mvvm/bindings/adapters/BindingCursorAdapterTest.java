package mobi.liason.mvvm.bindings.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Sets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import mobi.liason.mvvm.RobolectricTestRunnerWithInjection;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class BindingCursorAdapterTest {

    @Test
    public void newViewOptimizesTags(){
        final Context context = Robolectric.getShadowApplication().getApplicationContext();
        final ItemTypeBinding mockItemTypeBinding = mock(ItemTypeBinding.class);
        when(mockItemTypeBinding.getResourceIds()).thenReturn(Sets.newHashSet(1,2));

        final AdapterBinding mockAdapterBinding = mock(AdapterBinding.class);
        when(mockAdapterBinding.getItemTypeBinding(anyInt())).thenReturn(mockItemTypeBinding);

        final View mockView = mock(View.class);

        final View mockRootView = mock(View.class);
        when(mockRootView.findViewById(anyInt())).thenReturn(mockView);

        final LayoutInflater mockLayoutInflater = mock(LayoutInflater.class);
        when(mockLayoutInflater.inflate(anyInt(),any(ViewGroup.class),anyBoolean())).thenReturn(mockRootView);

        final Context mockContext = mock(Context.class);
        when(mockContext.getSystemService(anyString())).thenReturn(mockLayoutInflater);

        final Cursor mockCursor = mock(Cursor.class);
        final ViewGroup mockViewGroup = mock(ViewGroup.class);

        final BindingCursorAdapter bindingCursorAdapter = new BindingCursorAdapter(mockContext, mockAdapterBinding);
        bindingCursorAdapter.newView(mockContext,mockCursor, mockViewGroup);

        verify(mockRootView).setTag(1, mockView);
        verify(mockRootView).setTag(2, mockView);
    }

    @Test
    public void newViewInflatesCorrectView(){
        final Context context = Robolectric.getShadowApplication().getApplicationContext();

        final ItemTypeBinding mockItemTypeBinding1 = mock(ItemTypeBinding.class);
        when(mockItemTypeBinding1.getResourceIds()).thenReturn(Sets.newHashSet(0));
        when(mockItemTypeBinding1.getLayoutResourceId()).thenReturn(0);

        final ItemTypeBinding mockItemTypeBinding2 = mock(ItemTypeBinding.class);
        when(mockItemTypeBinding2.getResourceIds()).thenReturn(Sets.newHashSet(1));
        when(mockItemTypeBinding2.getLayoutResourceId()).thenReturn(1);

        final AdapterBinding mockAdapterBinding = mock(AdapterBinding.class);
        when(mockAdapterBinding.getItemTypeBinding(0)).thenReturn(mockItemTypeBinding1);
        when(mockAdapterBinding.getItemTypeBinding(1)).thenReturn(mockItemTypeBinding2);
        when(mockAdapterBinding.getItemType(any(Cursor.class))).thenReturn(1);


        final View mockView = mock(View.class);
        final View mockRootView1 = mock(View.class);
        when(mockRootView1.findViewById(anyInt())).thenReturn(mockView);

        final View mockRootView2 = mock(View.class);
        when(mockRootView2.findViewById(anyInt())).thenReturn(mockView);

        final LayoutInflater mockLayoutInflater = mock(LayoutInflater.class);
        when(mockLayoutInflater.inflate(eq(0),any(ViewGroup.class),anyBoolean())).thenReturn(mockRootView1);
        when(mockLayoutInflater.inflate(eq(1),any(ViewGroup.class),anyBoolean())).thenReturn(mockRootView2);

        final Context mockContext = mock(Context.class);
        when(mockContext.getSystemService(anyString())).thenReturn(mockLayoutInflater);

        final Cursor mockCursor = mock(Cursor.class);

        final ViewGroup mockViewGroup = mock(ViewGroup.class);

        final BindingCursorAdapter bindingCursorAdapter = new BindingCursorAdapter(mockContext, mockAdapterBinding);
        final View resultView = bindingCursorAdapter.newView(mockContext, mockCursor, mockViewGroup);

        assertThat(resultView).isEqualTo(mockRootView2);
    }


}