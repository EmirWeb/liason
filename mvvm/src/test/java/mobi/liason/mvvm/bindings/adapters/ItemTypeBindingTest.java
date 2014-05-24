package mobi.liason.mvvm.bindings.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.robolectric.Robolectric;

import java.util.HashSet;
import java.util.Set;

import mobi.liason.mvvm.RobolectricTestRunnerWithInjection;
import mobi.liason.mvvm.bindings.TextBinder;
import mobi.liason.mvvm.bindings.adapters.AdapterItemBinding;
import mobi.liason.mvvm.bindings.interfaces.Binding;
import mobi.liason.mvvm.bindings.interfaces.ColumnBinding;
import mobi.liason.mvvm.bindings.interfaces.ColumnResourceBinding;
import mobi.liason.mvvm.bindings.interfaces.DataBinding;
import mobi.liason.mvvm.bindings.interfaces.ResourceBinding;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class ItemTypeBindingTest {

    @Test
    public void getLayoutResourceIdReturnsSameLayoutResourceIdPassedInConstructor(){
        AdapterItemBinding adapterItemBinding = new AdapterItemBinding(1);
        int layoutResourceId = adapterItemBinding.getLayoutResourceId();
        assertThat(layoutResourceId).isEqualTo(1);

        adapterItemBinding = new AdapterItemBinding(2, new TextBinder(0,"COLUMN_NAME"));
        layoutResourceId = adapterItemBinding.getLayoutResourceId();
        assertThat(layoutResourceId).isEqualTo(2);


        final Set<Binding> bindings = new HashSet<Binding>();
        bindings.add(new TextBinder(0, "COLUMN_NAME_0"));
        bindings.add(new TextBinder(1, "COLUMN_NAME_1"));
        adapterItemBinding = new AdapterItemBinding(2, bindings);
        layoutResourceId = adapterItemBinding.getLayoutResourceId();
        assertThat(layoutResourceId).isEqualTo(2);
    }

    @Test
    public void getResourceIdsReturnsResourceIdsPassedInConstructorViaBindings(){
        AdapterItemBinding adapterItemBinding = new AdapterItemBinding(2, new TextBinder(0,"COLUMN_NAME"));
        Set<Integer> resourceIds = adapterItemBinding.getResourceIds();
        assertThat(resourceIds).containsExactly(0);


        final Set<Binding> bindings = new HashSet<Binding>();
        bindings.add(new TextBinder(0, "COLUMN_NAME_0"));
        bindings.add(new TextBinder(1, "COLUMN_NAME_1"));
        adapterItemBinding = new AdapterItemBinding(2, bindings);
        resourceIds = adapterItemBinding.getResourceIds();
        assertThat(resourceIds).containsExactly(0, 1);
    }

    @Test
    public void getResourceIdsReturnsResourceIdsPassedInMethodViaBindings(){
        AdapterItemBinding adapterItemBinding = new AdapterItemBinding(2);
        adapterItemBinding.addBinding(new TextBinder(0,"COLUMN_NAME"));

        Set<Integer> resourceIds = adapterItemBinding.getResourceIds();
        assertThat(resourceIds).containsExactly(0);


        adapterItemBinding = new AdapterItemBinding(2);
        adapterItemBinding.addBinding(new TextBinder(0, "COLUMN_NAME_0"));
        adapterItemBinding.addBinding(new TextBinder(1, "COLUMN_NAME_1"));

        resourceIds = adapterItemBinding.getResourceIds();
        assertThat(resourceIds).containsExactly(0, 1);
    }

    @Test
    public void bindCallsAllCallbacksForColumnResourceBinding(){
        final Context context = Robolectric.getShadowApplication().getApplicationContext();
        final AdapterItemBinding adapterItemBinding = new AdapterItemBinding(2);
        final ColumnResourceBinding mockColumnResourceBinding = mock(ColumnResourceBinding.class);

        when(mockColumnResourceBinding.getResourceIds()).thenReturn(Sets.newHashSet(0,1));
        when(mockColumnResourceBinding.getColumnNames()).thenReturn(Sets.newHashSet("COLUMN_1","COLUMN_2"));

        adapterItemBinding.addBinding(mockColumnResourceBinding);

        final Cursor mockCursor = mock(Cursor.class);

        final View mockView = mock(View.class);
        when(mockView.getTag(anyInt())).thenReturn(new TextView(context));

        adapterItemBinding.bind(context, mockView, mockCursor);

        final InOrder inOrder = inOrder(mockColumnResourceBinding);
        inOrder.verify(mockColumnResourceBinding).onBindStart(any(Context.class));
        inOrder.verify(mockColumnResourceBinding).onBindEnd(any(Context.class));

        verify(mockColumnResourceBinding, times(1)).onBind(any(Context.class), any(Cursor.class), any(View.class), eq(0));
        verify(mockColumnResourceBinding, times(1)).onBind(any(Context.class), any(Cursor.class), any(View.class), eq(1));

        verify(mockColumnResourceBinding, times(1)).onBind(any(Context.class), any(Cursor.class), any(View.class), eq(0), anyInt(), eq("COLUMN_1"));
        verify(mockColumnResourceBinding, times(1)).onBind(any(Context.class), any(Cursor.class), any(View.class), eq(0), anyInt(), eq("COLUMN_2"));
        verify(mockColumnResourceBinding, times(1)).onBind(any(Context.class), any(Cursor.class), any(View.class), eq(1), anyInt(), eq("COLUMN_1"));
        verify(mockColumnResourceBinding, times(1)).onBind(any(Context.class), any(Cursor.class), any(View.class), eq(1), anyInt(), eq("COLUMN_2"));

        verify(mockColumnResourceBinding, times(1)).onBind(any(Context.class), any(Cursor.class), anyInt(), eq("COLUMN_1"));
        verify(mockColumnResourceBinding, times(1)).onBind(any(Context.class), any(Cursor.class), anyInt(), eq("COLUMN_2"));

    }

    @Test
    public void bindCallsAllCallbacksForColumnBinding(){
        final Context context = Robolectric.getShadowApplication().getApplicationContext();
        final AdapterItemBinding adapterItemBinding = new AdapterItemBinding(2);
        final ColumnBinding mockColumnBinding = mock(ColumnBinding.class);

        when(mockColumnBinding.getColumnNames()).thenReturn(Sets.newHashSet("COLUMN_1","COLUMN_2"));

        adapterItemBinding.addBinding(mockColumnBinding);

        final Cursor mockCursor = mock(Cursor.class);

        final View mockView = mock(View.class);
        when(mockView.getTag(anyInt())).thenReturn(new TextView(context));

        adapterItemBinding.bind(context, mockView, mockCursor);

        final InOrder inOrder = inOrder(mockColumnBinding);
        inOrder.verify(mockColumnBinding).onBindStart(any(Context.class));
        inOrder.verify(mockColumnBinding).onBindEnd(any(Context.class));

        verify(mockColumnBinding, times(1)).onBind(any(Context.class), any(Cursor.class), anyInt(), eq("COLUMN_1"));
        verify(mockColumnBinding, times(1)).onBind(any(Context.class), any(Cursor.class), anyInt(), eq("COLUMN_2"));

    }

    @Test
    public void bindCallsAllCallbacksForResourceBinding(){
        final Context context = Robolectric.getShadowApplication().getApplicationContext();
        final AdapterItemBinding adapterItemBinding = new AdapterItemBinding(2);
        final ResourceBinding resourceBinding = mock(ResourceBinding.class);

        when(resourceBinding.getResourceIds()).thenReturn(Sets.newHashSet(0,1));

        adapterItemBinding.addBinding(resourceBinding);

        final Cursor mockCursor = mock(Cursor.class);

        final View mockView = mock(View.class);
        when(mockView.getTag(anyInt())).thenReturn(new TextView(context));

        adapterItemBinding.bind(context, mockView, mockCursor);

        final InOrder inOrder = inOrder(resourceBinding);
        inOrder.verify(resourceBinding).onBindStart(any(Context.class));
        inOrder.verify(resourceBinding).onBindEnd(any(Context.class));

        verify(resourceBinding, times(1)).onBind(any(Context.class), any(Cursor.class), any(View.class), eq(0));
        verify(resourceBinding, times(1)).onBind(any(Context.class), any(Cursor.class), any(View.class), eq(1));

    }

    @Test
    public void bindCallsAllCallbacksForDataBinding(){
        final Context context = Robolectric.getShadowApplication().getApplicationContext();
        final AdapterItemBinding adapterItemBinding = new AdapterItemBinding(2);
        final DataBinding mockDataBinding = mock(DataBinding.class);


        adapterItemBinding.addBinding(mockDataBinding);

        final Cursor mockCursor = mock(Cursor.class);

        final View mockView = mock(View.class);
        when(mockView.getTag(anyInt())).thenReturn(new TextView(context));

        adapterItemBinding.bind(context, mockView, mockCursor);

        final InOrder inOrder = inOrder(mockDataBinding);
        inOrder.verify(mockDataBinding).onBindStart(any(Context.class));
        inOrder.verify(mockDataBinding).onBindEnd(any(Context.class));

        verify(mockDataBinding, times(1)).onBind(any(Context.class), any(Cursor.class));

    }

}