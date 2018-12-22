package io.syslogic.github.activity;

import android.annotation.TargetApi;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import io.syslogic.github.R;
import io.syslogic.github.fragment.RepositoryFragment;
import io.syslogic.github.constants.Constants;

public class DetailActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    protected long itemId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /* obtain the itemId from intent extras */
        if(this.getIntent() != null) {
            Bundle extras = this.getIntent().getExtras();
            if(extras != null) {
                this.setItemId(extras.getLong(Constants.ARGUMENT_ITEM_ID));
            }
        }
        addFragment(savedInstanceState, R.id.layout_repository, RepositoryFragment.newInstance(getItemId()));
    }

    protected void setItemId(long value) {
        this.itemId = value;
    }

    public long getItemId() {
        return this.itemId;
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        this.currentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
