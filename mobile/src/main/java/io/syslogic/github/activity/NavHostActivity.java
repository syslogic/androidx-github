package io.syslogic.github.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.splashscreen.SplashScreen;

import io.syslogic.github.R;

/**
 * NavHost {@link BaseActivity} using {@link SplashScreen}.
 * @author Martin Zeitler
 */
public class NavHostActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        this.setContentView(R.layout.fragment_navhost);
    }
}
