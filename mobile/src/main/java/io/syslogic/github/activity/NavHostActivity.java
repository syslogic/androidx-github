package io.syslogic.github.activity;

import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.Nullable;

import io.syslogic.github.R;
import io.syslogic.github.fragment.NavHostFragment;

/**
 * Main NavHost Activity
 * @author Martin Zeitler
 * @version 1.0.0
**/
public class NavHostActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(savedInstanceState, R.id.fragment_navhost, new NavHostFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }
}
