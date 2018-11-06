package io.syslogic.github.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Objects;

import androidx.fragment.app.Fragment;
import io.syslogic.github.BuildConfig;

abstract public class BaseFragment extends Fragment {

    /** Debug Output */
    protected static final boolean mDebug = BuildConfig.DEBUG;

    public BaseFragment() {

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = Objects.requireNonNull(cm).getActiveNetworkInfo();
        if (info == null) {return false;}
        else {return true;}
    }
}
