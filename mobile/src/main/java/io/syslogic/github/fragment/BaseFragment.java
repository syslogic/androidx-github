package io.syslogic.github.fragment;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.R;
import io.syslogic.github.network.ConnectivityReceiver;
import io.syslogic.github.network.IConnectivityAware;

abstract public class BaseFragment extends Fragment implements IConnectivityAware {

    /** {@link Log} Tag */
    private final String LOG_TAG = BaseFragment.class.getSimpleName();

    /** Debug Output */
    static final boolean mDebug = BuildConfig.DEBUG;

    public BaseFragment() {

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
    void registerNetworkCallback(Context context, final IConnectivityAware listener) {
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
            String message = this.getContext().getResources().getString(R.string.debug_network_available);
            // Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            Log.w(LOG_TAG, message);
        }
    }

    @Override
    public void onNetworkLost() {
        if(mDebug && this.getContext() != null) {
            String message = this.getContext().getResources().getString(R.string.debug_network_lost);
            // Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            Log.w(LOG_TAG, message);
        }
    }

    @NonNull
    abstract public ViewDataBinding getDataBinding();
}
