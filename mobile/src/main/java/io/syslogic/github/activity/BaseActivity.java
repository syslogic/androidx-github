package io.syslogic.github.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.fragment.BaseFragment;

abstract public class BaseActivity extends AppCompatActivity {

    /** Debug Output */
    protected static final boolean mDebug = BuildConfig.DEBUG;

    /** the current {@link Fragment} */
    protected Fragment currentFragment = null;

    protected Fragment addFragment(@Nullable Bundle savedInstanceState, @IdRes int resId, @NonNull Fragment fragment) {

        FrameLayout frameLayout = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        frameLayout.setId(resId);

        this.setContentView(frameLayout, params);

        /* onOrientationChange() */
        if(savedInstanceState != null) {
            fragment = getSupportFragmentManager().getFragments().get(0);
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(resId, fragment).commit();
        }

        return fragment;
    }

    public ViewDataBinding getFragmentDataBinding() {
        BaseFragment fragment = (BaseFragment) this.getSupportFragmentManager().getFragments().get(0);
        return fragment.getDataBinding();
    }
}
