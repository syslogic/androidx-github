package io.syslogic.github.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.R;
import io.syslogic.github.constants.Constants;
import io.syslogic.github.model.User;
import io.syslogic.github.network.ConnectivityReceiver;
import io.syslogic.github.network.ConnectivityListener;
import io.syslogic.github.network.TokenCallback;
import io.syslogic.github.retrofit.GithubClient;
import io.syslogic.github.network.TokenHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Base Fragment
 * @author Martin Zeitler
 * @version 1.0.0
**/
abstract public class BaseFragment extends Fragment implements ConnectivityListener, TokenCallback {

    /** {@link Log} Tag */
    private final String LOG_TAG = BaseFragment.class.getSimpleName();

    /** Debug Output */
    static final boolean mDebug = BuildConfig.DEBUG;

    @NonNull
    Boolean contentLoaded = false;

    User currentUser = null;

    private String accessToken = null;

    public BaseFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.registerNetworkCallback(this.getContext(), this);
        } else {
            this.registerBroadcastReceiver(this.getContext());
        }

        if (getContext() != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Activity activity = (Activity) getContext();

            /* for testing purposes only: */
            this.accessToken = this.getAccessToken(this.getContext());

            if (activity.checkSelfPermission(Manifest.permission.ACCOUNT_MANAGER) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.ACCOUNT_MANAGER}, Constants.REQUESTCODE_ADD_ACCESS_TOKEN);
            } else {
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

    @SuppressWarnings("deprecation")
    public boolean isNetworkAvailable(@NonNull Context context) {
        ConnectivityManager cm = getConnectivityManager(context);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerNetworkCallback(Context context, final ConnectivityListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {return;}
        ConnectivityManager cm = getConnectivityManager(context);
        cm.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onLost(@NonNull Network network) {
                listener.onNetworkLost();
            }
            @Override
            public void onAvailable(@NonNull Network network) {
                listener.onNetworkAvailable();
            }
        });
    }

    /** required < API 24 Nougat */
    @SuppressWarnings(value = "deprecation")
    private void registerBroadcastReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        ConnectivityReceiver mReceiver = new ConnectivityReceiver(context);
        context.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onNetworkAvailable() {
        if(mDebug && this.getContext() != null) {
            Log.w(LOG_TAG, this.getContext().getResources().getString(R.string.debug_network_present));
        }
    }

    @Override
    public void onNetworkLost() {
        if(mDebug && this.getContext() != null) {
            Log.w(LOG_TAG, this.getContext().getResources().getString(R.string.debug_network_absent));
        }
    }

    void setUser(@NonNull String accessToken, @Nullable final TokenCallback listener) {

        Call<User> api = GithubClient.getUser(accessToken);
        if (mDebug) {Log.w(LOG_TAG, api.request().url() + "");}

        api.enqueue(new Callback<User>() {

            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                switch(response.code()) {

                    case 200: {
                        if (response.body() != null) {
                            User item = response.body();
                            if(mDebug) {Log.w(LOG_TAG, "logged in as: " + item.getLogin());}
                            if(listener != null) {listener.onLogin(item);}
                            setCurrentUser(item);
                        }
                        break;
                    }

                    case 403: {
                        if (response.errorBody() != null) {
                            try {
                                String errors = response.errorBody().string();
                                JsonObject jsonObject = (new JsonParser()).parse(errors).getAsJsonObject();
                                String message = jsonObject.get("message").toString();
                                if(mDebug) {
                                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                    Log.e(LOG_TAG, message);
                                }
                            } catch (IOException e) {
                                if(mDebug) {Log.e(LOG_TAG, "" + e.getMessage());}
                            }
                        }
                        break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                // if (mDebug) {Log.e(LOG_TAG, t.getMessage());}
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(requestCode == Constants.REQUESTCODE_ADD_ACCESS_TOKEN) {
                //this.setUser(this.accessToken, this);
            }
        }
    }

    @NonNull
    abstract public ViewDataBinding getDataBinding();

    String getAccessToken(Context context) {
        return TokenHelper.getAccessToken(context);
    }

    @Nullable
    User getCurrentUser() {
        return currentUser;
    }

    abstract protected void setDataBinding(@NonNull ViewDataBinding binding);

    abstract protected void setCurrentUser(@NonNull User value);
}
