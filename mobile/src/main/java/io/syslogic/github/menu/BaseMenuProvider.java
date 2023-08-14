package io.syslogic.github.menu;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.lang.ref.WeakReference;

import io.syslogic.github.activity.BaseActivity;

/**
 * Abstract Base {@link MenuProvider}
 *
 * @author Martin Zeitler
 */
abstract public class BaseMenuProvider implements MenuProvider {

    WeakReference<BaseActivity> mContext;

    /** Constructor */
    public BaseMenuProvider(@NonNull BaseActivity activity) {
        this.mContext = new WeakReference<>(activity);
    }

    @Nullable
    protected NavController getNavController() {
        if (this.mContext.get().getNavhostFragment() != null) {
            NavHostFragment fragment = this.mContext.get().getNavhostFragment();
            if (fragment != null) {return fragment.getNavController();}
        }
        return null;
    }

    abstract public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater);
    abstract public boolean onMenuItemSelected(@NonNull MenuItem item);
}
