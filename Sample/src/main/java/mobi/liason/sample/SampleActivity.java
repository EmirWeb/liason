package mobi.liason.sample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import mobi.liason.loaders.ActivityBindingManager;
import mobi.liason.loaders.BindDefinition;
import mobi.liason.loaders.support.SupportActivityBindingManager;

/**
 * Created by Emir on 2014-08-23.
 */
public class SampleActivity extends FragmentActivity {

    private SupportActivityBindingManager mActivityBindingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityBindingManager = new SupportActivityBindingManager(this);
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
