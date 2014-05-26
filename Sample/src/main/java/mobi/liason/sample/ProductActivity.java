package mobi.liason.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import mobi.liason.loaders.ActivityBindingManager;
import mobi.liason.sample.product.bindings.ProductItemBinding;
import mobi.liason.sample.product.bindings.ProductTaskStateBinding;

public class ProductActivity extends Activity {

    private ActivityBindingManager mActivityBindingManager;

    private static final class Extras {
        public static final String ID = "id";
    }

    public static void startActivity(final Activity activity, final long id) {
        final Intent intent = new Intent(activity, ProductActivity.class);
        intent.putExtra(Extras.ID, id);
        activity.startActivity(intent);
    }

    public long getId() {
        final Intent intent = getIntent();
        return intent.getLongExtra(Extras.ID, -1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        mActivityBindingManager = new ActivityBindingManager(this);

        final long id = getId();

        final ProductItemBinding productItemBinding = new ProductItemBinding(this, id);
        mActivityBindingManager.addBindDefinition(productItemBinding);

        final ProductTaskStateBinding productTaskStateBinding = new ProductTaskStateBinding(this, id);
        mActivityBindingManager.addBindDefinition(productTaskStateBinding);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Context context = getApplicationContext();
        mActivityBindingManager.onStart(context);
    }

    @Override
    protected void onStop() {
        super.onStop();
        final Context context = getApplicationContext();
        mActivityBindingManager.onStop(context);
    }
}

