package io.syslogic.github.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import io.syslogic.github.fragment.BaseFragment;
import io.syslogic.github.model.User;

/**
 * Base Activity
 * @author Martin Zeitler
 * @version 1.0.0
**/
abstract public class BaseActivity extends AppCompatActivity {

    protected Fragment currentFragment = null;

    protected User currentUser = null;

    @NonNull
    public ViewDataBinding getFragmentDataBinding() {
        BaseFragment fragment = (BaseFragment) this.getSupportFragmentManager().getFragments().get(0);
        return fragment.getDataBinding();
    }

    public void setUser(User value) {
        this.currentUser = value;
    }

    public User getUser() {
        return this.currentUser;
    }

    protected void addFragment(@Nullable Bundle savedInstanceState, @NonNull @IdRes Integer resId, @NonNull Fragment fragment) {

        FrameLayout frameLayout = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        frameLayout.setId(resId);

        this.setContentView(frameLayout, params);

        if(savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            this.currentFragment = fragment;
            ft.add(resId, fragment).commit();
        }
    }
}
