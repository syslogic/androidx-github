package io.syslogic.github.activity;

import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.Nullable;

import io.syslogic.github.R;

/**
 * Main NavHost Activity
 * @author Martin Zeitler
 * @version 1.0.0
**/
public class NavHostActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.fragment_navhost);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }
}
