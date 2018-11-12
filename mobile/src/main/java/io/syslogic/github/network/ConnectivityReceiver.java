package io.syslogic.github.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import io.syslogic.github.BuildConfig;
import io.syslogic.github.activity.BaseActivity;
import io.syslogic.github.activity.DetailActivity;
import io.syslogic.github.activity.MainActivity;
import io.syslogic.github.fragment.RepositoriesFragment;
import io.syslogic.github.fragment.RepositoryFragment;

/**
 * Apps targeting Android 7.0 (API level 24) and higher do not receive CONNECTIVITY_ACTION broadcasts
 * if they declare the broadcast receiver in their manifest. Apps will still receive CONNECTIVITY_ACTION broadcasts
 * if they register their BroadcastReceiver with Context.registerReceiver() and that context is still valid.
**/
public class ConnectivityReceiver extends android.content.BroadcastReceiver {

    /** {@link Log} Tag */
    private static final String LOG_TAG = ConnectivityReceiver.class.getSimpleName();

    /** Debug Output */
    protected static final boolean mDebug = BuildConfig.DEBUG;

    public ConnectivityReceiver(@NonNull Context context) {

    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        if(mDebug) {Log.d(LOG_TAG, "onReceive: "+ intent.getAction());}
        BaseActivity activity = ((BaseActivity) context);
        if(activity instanceof MainActivity) {
            RepositoriesFragment fragment = (RepositoriesFragment) activity.getSupportFragmentManager().getFragments().get(0);
            if(fragment.isNetworkAvailable(context)) {fragment.onNetworkAvailable();} else {fragment.onNetworkLost();}
        } else if (activity instanceof DetailActivity) {
            RepositoryFragment fragment = (RepositoryFragment) activity.getSupportFragmentManager().getFragments().get(0);
            if(fragment.isNetworkAvailable(context)) {fragment.onNetworkAvailable();} else {fragment.onNetworkLost();}
        }
    }
}
