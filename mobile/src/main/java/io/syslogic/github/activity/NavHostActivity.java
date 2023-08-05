package io.syslogic.github.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.splashscreen.SplashScreen;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import io.syslogic.github.R;
import io.syslogic.github.databinding.FragmentNavHostBinding;

/**
 * The NavHost {@link BaseActivity} using {@link SplashScreen}
 *
 * @author Martin Zeitler
 */
public class NavHostActivity extends BaseActivity {

    /** Log Tag */
    @SuppressWarnings("unused")
    private static final String LOG_TAG = NavHostActivity.class.getSimpleName();

    /** layout resId kept for reference */
    @SuppressWarnings("unused")
    private static final int resId = R.layout.fragment_nav_host;

    private NavController navController;

    /** Data Binding */
    private FragmentNavHostBinding mDataBinding;

    /**
     * Note: It causes a theme-conflict, when switching the theme from/to dark mode, because
     * AppTheme.Splash does not extend from Theme.MaterialComponents.DayNight.NoActionBar.Bridge.
     * This is why the workaround below is required.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        /* When switching the theme to/from dark mode (on all subsequent executions). */
        if (savedInstanceState != null) {this.setTheme(R.style.Theme_AppTheme);}
        super.onCreate(savedInstanceState);

        /* When starting the Activity for the first time. */
        if (savedInstanceState == null) {SplashScreen.installSplashScreen(this);}

        this.setDataBinding(FragmentNavHostBinding
                .inflate(getLayoutInflater(), findViewById(android.R.id.content), true));
        this.setNavController();
    }

    private void setNavController() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_navhost);
        if (navHostFragment != null) {
            this.navController = NavHostFragment.findNavController(navHostFragment);
        }
    }

    public NavController getNavController() {
        return this.navController;
    }

    @NonNull
    @VisibleForTesting
    @SuppressWarnings("unused")
    private FragmentNavHostBinding getDataBinding() {
        return this.mDataBinding;
    }

    private void setDataBinding(@NonNull ViewDataBinding binding) {
        this.mDataBinding = (FragmentNavHostBinding) binding;
    }
}
