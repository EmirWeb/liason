package mobi.liason.sample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import mobi.liason.loaders.ActivityBindingManager;
import mobi.liason.loaders.BindDefinition;

/**
 * Created by Emir on 2014-08-23.
 */
public class SampleActivity extends Activity {

    private ActivityBindingManager mActivityBindingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityBindingManager = new ActivityBindingManager(this);
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

    protected void addBindDefinition(final BindDefinition bindDefinition) {
        mActivityBindingManager.addBindDefinition(bindDefinition);
    }
}
