package io.syslogic.github.provider;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.MenuHost;

import io.syslogic.github.R;
import io.syslogic.github.activity.BaseActivity;

/**
 * Workflows {@link BaseMenuProvider}
 *
 * @author Martin Zeitler
 */
public class WorkflowsMenuProvider extends BaseMenuProvider {

    /** Constructor */
    public WorkflowsMenuProvider(@NonNull BaseActivity activity) {
        super(activity);
    }

    /**
     * Called by the {@link MenuHost} to allow the {@link BaseMenuProvider} to inflate {@link MenuItem}s into the menu.
     *
     * @param menu     the menu to inflate the new menu items into
     * @param inflater the inflater to be used to inflate the updated menu
     */
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.workflows, menu);
    }

    /**
     * Called by the {@link MenuHost} when a {@link MenuItem} is selected from the menu.
     *
     * @param item the menu item that was selected
     * @return {@code true} if the given menu item is handled by this menu provider, {@code false} otherwise
     */
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
