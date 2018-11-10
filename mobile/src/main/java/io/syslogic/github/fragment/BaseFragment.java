package io.syslogic.github.fragment;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.network.ConnectivityReceiver;
import io.syslogic.github.network.IConnectivityListener;

abstract public class BaseFragment extends Fragment {

    /** {@link Log} Tag */
    private static final String LOG_TAG = BaseFragment.class.getSimpleName();

    /** Debug Output */
    static final boolean mDebug = BuildConfig.DEBUG;

    public BaseFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected static ConnectivityManager getConnectivityManager(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm);
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = getConnectivityManager(context);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void registerNetworkCallback(Context context, final IConnectivityListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {return;}
        ConnectivityManager cm = getConnectivityManager(context);
        cm.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onLost(Network network) {
                listener.onNetworkLost();
            }
            @Override
            public void onAvailable(Network network) {
                listener.onNetworkAvailable();
            }
        });
    }

    /** required < API 24 Nougat */
    @SuppressWarnings("deprecation")
    protected void registerBroadcastReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        ConnectivityReceiver mReceiver = new ConnectivityReceiver(context);
        context.registerReceiver(mReceiver, intentFilter);
    }

    abstract public ViewDataBinding getDataBinding();
}
