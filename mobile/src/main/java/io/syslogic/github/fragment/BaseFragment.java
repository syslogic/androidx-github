package io.syslogic.github.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.R;
import io.syslogic.github.Constants;
import io.syslogic.github.model.User;
import io.syslogic.github.network.ConnectivityReceiver;
import io.syslogic.github.network.ConnectivityListener;
import io.syslogic.github.network.TokenCallback;
import io.syslogic.github.network.TokenHelper;
import io.syslogic.github.retrofit.GithubClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Base {@link Fragment}
 *
 * @author Martin Zeitler
 */
abstract public class BaseFragment extends Fragment implements ConnectivityListener {

    /** Log Tag */
    private final String LOG_TAG = BaseFragment.class.getSimpleName();

    /** Debug Output */
    static final boolean mDebug = BuildConfig.DEBUG;

    @Nullable User currentUser = null;

    @Nullable SharedPreferences prefs;
    @NonNull Boolean contentLoaded = false;

    /** Constructor */
    public BaseFragment() {}

    @Override
    @SuppressLint("NewApi")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.registerNetworkCallback(requireContext(), BaseFragment.this);
        } else {
            this.registerBroadcastReceiver(requireContext());
        }
    }

    @NonNull
    private static ConnectivityManager getConnectivityManager(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm);
    }

    @SuppressWarnings({"deprecation", "RedundantSuppression"})
    public static boolean isNetworkAvailable(@Nullable Context context) {
        if (context == null)  {return false;}
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                        return true;
                    }
                }
            } else {
                try {
                    android.net.NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Log.d("", "network available");
                        return true;
                    }
                } catch (Exception e) {
                    Log.e("", "" + e.getMessage());
                }
            }
        }
        Log.i("update_status","Network is available : FALSE ");
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerNetworkCallback(Context context, final ConnectivityListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {return;}
        ConnectivityManager cm = getConnectivityManager(context);
        cm.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override public void onLost(@NonNull android.net.Network network) {
                listener.onNetworkLost();
            }
            @Override public void onAvailable(@NonNull android.net.Network network) {
                listener.onNetworkAvailable();
            }
        });
    }

    /** required < API 24 Nougat */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerBroadcastReceiver(@NonNull Context context) {
        IntentFilter intentFilter = new IntentFilter(Constants.CONNECTIVITY_ACTION);
        ConnectivityReceiver mReceiver = new ConnectivityReceiver();
        context.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onNetworkAvailable() {
        if (mDebug && this.getContext() != null) {
            Log.d(LOG_TAG, this.getContext().getResources().getString(R.string.debug_network_present));
        }
    }

    @Override
    public void onNetworkLost() {
        if (mDebug && this.getContext() != null) {
            Log.d(LOG_TAG, this.getContext().getResources().getString(R.string.debug_network_absent));
        }
    }

    @Override
    @SuppressWarnings({"deprecation", "RedundantSuppression"})
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //noinspection StatementWithEmptyBody
            if (requestCode == Constants.REQUESTCODE_ADD_ACCESS_TOKEN) {
                // setUser(this.accessToken, BaseFragment.this);
            }
        }
    }

    @NonNull
    abstract public ViewDataBinding getDataBinding();

    abstract protected void setDataBinding(@NonNull ViewDataBinding binding);

    @Nullable
    protected String getPersonalAccessToken() {
        return TokenHelper.getAccessToken(requireContext());
    }

    @Nullable
    protected User getCurrentUser() {
        return currentUser;
    }

    protected void setCurrentUser(@Nullable User value) {
        this.currentUser = value;
    }

    protected void setUser(@NonNull String accessToken, @Nullable final TokenCallback listener) {

        Call<User> api = GithubClient.getUser(accessToken);
        if (mDebug) {Log.w(LOG_TAG, api.request().url() + "");}

        api.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                switch (response.code()) {
                    case 200 -> {
                        if (response.body() != null) {
                            User item = response.body();
                            if (mDebug) {
                                String formatString = requireContext().getResources().getString(R.string.debug_token_auth);
                                String message = String.format(formatString, item.getLogin());
                                Log.d("Github API", message);
                            }
                            if (listener != null) {
                                listener.onLogin(item);
                            }
                            setCurrentUser(item);
                        }
                    }
                    case 403 -> {
                        if (response.errorBody() != null) {
                            try {
                                String errors = response.errorBody().string();
                                JsonObject jsonObject = JsonParser.parseString(errors).getAsJsonObject();
                                String message = jsonObject.get("message").toString();
                                if (mDebug) {
                                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                    Log.e(LOG_TAG, message);
                                }
                            } catch (IOException e) {
                                if (mDebug) {
                                    Log.e(LOG_TAG, "" + e.getMessage());
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                // if (mDebug) {Log.e(LOG_TAG, t.getMessage());}
            }
        });
    }
}
