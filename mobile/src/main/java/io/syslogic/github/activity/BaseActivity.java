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
import androidx.navigation.fragment.NavHostFragment;

import io.syslogic.github.R;
import io.syslogic.github.fragment.BaseFragment;

/**
 * Base Activity
 * @author Martin Zeitler
 */
abstract public class BaseActivity extends AppCompatActivity {

    @Nullable
    protected Fragment currentFragment = null;

    @Nullable
    public ViewDataBinding getFragmentDataBinding() {
        if(this.getCurrentFragment() instanceof BaseFragment) {
            return ((BaseFragment) this.getCurrentFragment()).getDataBinding();
        } else {
            return null;
        }
    }

    public NavHostFragment getNavhostFragment() {
        return (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navhost);
    }

    @Nullable
    public Fragment getCurrentFragment() {
        NavHostFragment navHostFragment = getNavhostFragment();
        return navHostFragment == null ? null : navHostFragment.getChildFragmentManager().getFragments().get(0);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (this.currentFragment != null) {
            this.currentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    protected void addFragment(@Nullable Bundle savedInstanceState, @NonNull @IdRes Integer resId, @NonNull Fragment fragment) {

        FrameLayout frameLayout = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        frameLayout.setId(resId);

        this.setContentView(frameLayout, params);

        if(savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            this.currentFragment = fragment;
            ft.add(resId, fragment).commit();
        }
    }
}
