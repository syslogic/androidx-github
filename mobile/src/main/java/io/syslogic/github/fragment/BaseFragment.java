package io.syslogic.github.fragment;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.R;
import io.syslogic.github.network.ConnectivityReceiver;
import io.syslogic.github.network.ConnectivityListener;
import io.syslogic.github.retrofit.TokenHelper;

/**
 * Base Fragment
 * @author Martin Zeitler
 * @version 1.0.0
**/
abstract public class BaseFragment extends Fragment implements ConnectivityListener {

    /** {@link Log} Tag */
    private final String LOG_TAG = BaseFragment.class.getSimpleName();

    /** Debug Output */
    static final boolean mDebug = BuildConfig.DEBUG;

    protected String accessToken = null;

    public BaseFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext() != null) {
            if (getContext().checkSelfPermission(Manifest.permission.ACCOUNT_MANAGER) == PackageManager.PERMISSION_GRANTED) {
                this.accessToken = this.getAccessToken(this.getContext());
            }
        } else {
            this.accessToken = this.getAccessToken(this.getContext());
        }
    }

    private static ConnectivityManager getConnectivityManager(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm);
    }

    public boolean isNetworkAvailable(@NonNull Context context) {
        ConnectivityManager cm = getConnectivityManager(context);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void registerNetworkCallback(Context context, final ConnectivityListener listener) {
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
    void registerBroadcastReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        ConnectivityReceiver mReceiver = new ConnectivityReceiver(context);
        context.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onNetworkAvailable() {
        if(mDebug && this.getContext() != null) {
            String message = this.getContext().getResources().getString(R.string.debug_network_present);
            Log.w(LOG_TAG, message);
        }
    }

    @Override
    public void onNetworkLost() {
        if(mDebug && this.getContext() != null) {
            String message = this.getContext().getResources().getString(R.string.debug_network_absent);
            Log.w(LOG_TAG, message);
        }
    }

    @NonNull
    abstract public ViewDataBinding getDataBinding();

    String getAccessToken(Context context) {
        return TokenHelper.getAccessToken(context);
    }
}
