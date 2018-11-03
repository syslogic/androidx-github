package io.syslogic.github.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import io.syslogic.github.R;
import io.syslogic.github.fragment.RepositoriesFragment;

public class MainActivity extends AppCompatActivity {

    /** layout res id */
    private final int resId = R.id.layout_repositories;

    /** current {@link Fragment} */
    protected RepositoriesFragment currentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FrameLayout frame = new FrameLayout(this);
        frame.setId(this.resId);
        setContentView(frame, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        if (savedInstanceState == null) {
            this.currentFragment = RepositoriesFragment.newInstance();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(this.resId, this.currentFragment, "repositories").commit();
        } else {

            /* onOrientationChange() */
            this.currentFragment = (RepositoriesFragment) getSupportFragmentManager().findFragmentByTag("repositories");
        }
    }
}
