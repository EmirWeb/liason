package mobi.liason.sample;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import mobi.liason.loaders.BindingManager;
import mobi.liason.mvvm.bindings.adapters.AdapterBinding;
import mobi.liason.sample.bindings.ProductsAdapterBinding;
import mobi.liason.sample.bindings.ProductsTaskStateBinding;

public class ProductActivity extends Activity {

    private BindingManager mBindingManager;

    private static final class Extras {
        public static final String ID = "id";
    }

    public static void startActivity(final Activity activity, final long id){
        final Intent intent = new Intent(activity, ProductActivity.class);
        intent.putExtra(Extras.ID, id);
        activity.startActivity(intent);
    }

    public String getId(){
        final Intent intent = getIntent();
        return intent.getStringExtra(Extras.ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        final Context context = getApplicationContext();
        final LoaderManager loaderManager = getLoaderManager();
        mBindingManager = new BindingManager(context, loaderManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Context context = getApplicationContext();
        mBindingManager.onStart(context);
    }

    @Override
    protected void onStop() {
        super.onStop();
        final Context context = getApplicationContext();
        mBindingManager.onStop(context);
    }
}

