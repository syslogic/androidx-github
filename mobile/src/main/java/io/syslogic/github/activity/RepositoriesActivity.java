package io.syslogic.github.activity;

import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.syslogic.github.R;
import io.syslogic.github.fragment.RepositoriesFragment;

/**
 * Main Repositories Activity
 * @author Martin Zeitler
 */
public class RepositoriesActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(savedInstanceState, R.id.layout_repositories, RepositoriesFragment.newInstance());
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }
}
