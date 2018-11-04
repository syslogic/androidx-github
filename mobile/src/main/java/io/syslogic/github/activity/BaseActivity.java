package io.syslogic.github.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import io.syslogic.github.BuildConfig;

abstract public class BaseActivity extends AppCompatActivity {

    /** Debug Output */
    protected static final boolean mDebug = BuildConfig.DEBUG;

    protected Fragment addFragment(@Nullable Bundle savedInstanceState, @IdRes int resId, @NonNull Fragment fragment, @NonNull String viewTag) {

        FrameLayout frameLayout = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        frameLayout.setId(resId);

        setContentView(frameLayout, params);

        if (savedInstanceState == null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(resId, fragment, viewTag).commit();

        } else {

            /* onOrientationChange() */
            fragment = getSupportFragmentManager().findFragmentByTag(viewTag);
        }
        return fragment;
    }
}
