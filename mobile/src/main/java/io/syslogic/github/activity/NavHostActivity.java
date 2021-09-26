package io.syslogic.github.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import io.syslogic.github.R;

/**
 * Main NavHost Activity
 * @author Martin Zeitler
 */
public class NavHostActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.fragment_navhost);
    }
}
