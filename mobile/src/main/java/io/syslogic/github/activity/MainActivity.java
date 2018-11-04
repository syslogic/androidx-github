package io.syslogic.github.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import io.syslogic.github.R;
import io.syslogic.github.fragment.RepositoriesFragment;

public class MainActivity extends BaseActivity {

    /** {@link Log} Tag */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /** the current {@link Fragment} */
    protected RepositoriesFragment currentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.currentFragment = (RepositoriesFragment) addFragment(savedInstanceState, R.id.layout_repositories, RepositoriesFragment.newInstance(), "repositories");
    }
}
