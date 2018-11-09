package io.syslogic.github.activity;

import android.os.Bundle;
import android.util.Log;

import io.syslogic.github.R;
import io.syslogic.github.fragment.RepositoriesFragment;

public class MainActivity extends BaseActivity {

    /** {@link Log} Tag */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.currentFragment = (RepositoriesFragment) addFragment(savedInstanceState, R.id.fragment_repositories, RepositoriesFragment.newInstance());
    }
}
