package io.syslogic.githubtrends.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import io.syslogic.githubtrends.BuildConfig;
import io.syslogic.githubtrends.R;
import io.syslogic.githubtrends.view.RepositoryFragment;
import io.syslogic.githubtrends.constants.Constants;

public class DetailActivity extends AppCompatActivity {

    /** {@link Log} Tag */
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    /** Debug Output */
    protected static final boolean mDebug = BuildConfig.DEBUG;

    /** layout res id */
    private final int resId = R.id.layout_repository;

    /** current {@link Fragment} */
    protected RepositoryFragment currentFragment = null;

    protected long itemId = -1;

    /* Constructor */
    public DetailActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /* obtain the itemId from arguments */
        if(this.getIntent() != null) {
            Bundle extras = this.getIntent().getExtras();
            if(extras != null) {
                this.setItemId(extras.getLong(Constants.ARGUMENT_ITEM_ID));
            }
        }

        FrameLayout frame = new FrameLayout(this);
        frame.setId(this.resId);
        setContentView(frame, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        if (savedInstanceState == null) {

            this.currentFragment = RepositoryFragment.newInstance(this.getItemId());

            Bundle args = new Bundle();
            args.putLong(Constants.ARGUMENT_ITEM_ID, this.getItemId());
            this.currentFragment.setArguments(args);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(this.resId, this.currentFragment, "repository").commit();

        } else {

            /* onOrientationChange() */
            this.currentFragment = (RepositoryFragment) getSupportFragmentManager().findFragmentByTag("repository");
        }
    }

    protected void setItemId(long value) {
        this.itemId = value;
    }

    public long getItemId() {
        return this.itemId;
    }
}
