package io.syslogic.github.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.fragment.NavHostFragment;

import io.syslogic.github.R;
import io.syslogic.github.fragment.BaseFragment;
import io.syslogic.github.menu.BaseMenuProvider;

/**
 * The Base {@link AppCompatActivity}
 *
 * @author Martin Zeitler
 */
abstract public class BaseActivity extends AppCompatActivity {

    /** Menu Provider */
    BaseMenuProvider mMenuProvider = null;

    public BaseActivity() {}

    @Nullable
    protected BaseFragment currentFragment = null;

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
    @SuppressWarnings({"deprecation", "RedundantSuppression"})
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (this.currentFragment != null) {
            this.currentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void setMenuProvider(@NonNull BaseMenuProvider menuProvider) {
        if (this.mMenuProvider != null) {removeMenuProvider(this.mMenuProvider);}
        addMenuProvider(menuProvider, this, Lifecycle.State.RESUMED);
        this.mMenuProvider = menuProvider;
    }
}
