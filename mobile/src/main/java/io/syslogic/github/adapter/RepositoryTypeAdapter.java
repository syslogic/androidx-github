package io.syslogic.github.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import io.syslogic.github.R;

/**
 * Repository-Type {@link BaseArrayAdapter}.
 *
 * @author Martin Zeitler
 */
public class RepositoryTypeAdapter extends BaseArrayAdapter {
    public RepositoryTypeAdapter(@NonNull Context context) {
        super(context);
        this.setItems(context, R.array.repository_type_keys, R.array.repository_type_values);
    }
}
