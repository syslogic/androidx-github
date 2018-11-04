package io.syslogic.github.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import io.syslogic.github.R;
import io.syslogic.github.fragment.RepositoryFragment;
import io.syslogic.github.constants.Constants;

public class DetailActivity extends BaseActivity {

    /** {@link Log} Tag */
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    /** current {@link Fragment} */
    protected RepositoryFragment currentFragment = null;

    protected long itemId = -1;

    /* Constructor */
    public DetailActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /* obtain the itemId from intent extras */
        if(this.getIntent() != null) {
            Bundle extras = this.getIntent().getExtras();
            if(extras != null) {
                this.setItemId(extras.getLong(Constants.ARGUMENT_ITEM_ID));
            } else if (mDebug) {
                Log.e(LOG_TAG, "intent extras had no itemId");
            }
        }
        this.currentFragment = (RepositoryFragment) addFragment(savedInstanceState, R.id.layout_repository, RepositoryFragment.newInstance(getItemId()), "repository");
    }

    protected void setItemId(long value) {
        this.itemId = value;
    }

    public long getItemId() {
        return this.itemId;
    }
}
