package io.syslogic.github.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import io.syslogic.github.R;
import io.syslogic.github.fragment.RepositoryFragment;

public class MainActivity extends BaseActivity implements NavHost {

    /** {@link Log} Tag */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.currentFragment = (NavHostFragment) addFragment(savedInstanceState, R.id.fragment_navhost, NavHostFragment.create(R.navigation.graph));
    }

    @NonNull
    @Override
    public NavController getNavController() {
        return Navigation.findNavController(this, R.id.fragment_navhost);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = this.currentFragment.getChildFragmentManager().getFragments().get(0);
        if (fragment instanceof RepositoryFragment) {
            this.getNavController().navigateUp();
        } else {
            super.onBackPressed();
            MainActivity.this.finish();
        }
    }
}
