package io.syslogic.github.fragment;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ViewFlipper;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.model.RateLimit;
import io.syslogic.github.model.RateLimits;
import io.syslogic.github.network.ConnectivityReceiver;
import io.syslogic.github.network.IConnectivityListener;
import io.syslogic.github.retrofit.GithubClient;
import io.syslogic.github.retrofit.GithubService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

abstract public class BaseFragment extends Fragment {

    /** {@link Log} Tag */
    private static final String LOG_TAG = BaseFragment.class.getSimpleName();

    /** Debug Output */
    static final boolean mDebug = BuildConfig.DEBUG;

    /** the {@link android.widget.ViewFlipper} */
    protected ViewFlipper mViewFlipper;

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

    protected void getSearchQuota() {

        GithubService service = GithubClient.getService();
        Call<RateLimits> api = service.getRateLimits();
        if (mDebug) {Log.w(LOG_TAG, api.request().url() + "");}

        api.enqueue(new Callback<RateLimits>() {

            @Override
            public void onResponse(@NonNull Call<RateLimits> call, @NonNull Response<RateLimits> response) {
                switch(response.code()) {
                    case 200: {
                        if (response.body() != null) {
                            RateLimits items = response.body();
                            RateLimit search = items.getResources().getSearch();
                            if(mDebug) {
                                long seconds = Math.round((new Date(search.getReset() * 1000).getTime() - new Date().getTime()) / 1000);
                                String quota = String.format(Locale.getDefault(), "search quota: %d / %d. reset in %d seconds.", search.getRemaining(), search.getLimit(), seconds);
                                Log.d(LOG_TAG, quota);
                            }
                        }
                        break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RateLimits> call, @NonNull Throwable t) {
                if (mDebug) {Log.e(LOG_TAG, t.getMessage());}
            }
        });
    }
}
