package io.syslogic.github.activity;

import android.os.Bundle;
import android.util.Log;

import io.syslogic.github.R;
import io.syslogic.github.fragment.RepositoryFragment;
import io.syslogic.github.constants.Constants;

public class DetailActivity extends BaseActivity {

    /** {@link Log} Tag */
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    protected long itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /* obtain the itemId from intent extras */
        if(this.getIntent() != null) {
            Bundle extras = this.getIntent().getExtras();
            if(extras != null) {
                this.setItemId(extras.getLong(Constants.ARGUMENT_ITEM_ID));
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
