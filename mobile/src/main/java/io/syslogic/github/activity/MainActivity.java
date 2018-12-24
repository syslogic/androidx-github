package io.syslogic.github.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import io.syslogic.github.R;
import io.syslogic.github.fragment.RepositoriesFragment;

/**
 * Main Activity
 * @author Martin Zeitler
 * @version 1.0.0
**/
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(savedInstanceState, R.id.layout_repositories, RepositoriesFragment.newInstance());
    }
}
