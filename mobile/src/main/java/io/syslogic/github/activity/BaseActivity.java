package io.syslogic.github.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.appcompat.app.AppCompatActivity;
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
    public NavHostFragment getNavhostFragment() {
        return (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navhost);
    }

    @Nullable
    public Fragment getCurrentFragment() {
        NavHostFragment navHostFragment = getNavhostFragment();
        return navHostFragment == null ? null : navHostFragment.getChildFragmentManager().getFragments().get(0);
    }

    @Nullable
    public ViewDataBinding getFragmentDataBinding() {
        if (this.getCurrentFragment() instanceof BaseFragment) {
            return ((BaseFragment) this.getCurrentFragment()).getDataBinding();
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (this.currentFragment != null) {
            this.currentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
