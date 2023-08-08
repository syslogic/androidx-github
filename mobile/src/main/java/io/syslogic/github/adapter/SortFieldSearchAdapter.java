package io.syslogic.github.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import io.syslogic.github.R;

/**
 * Sort-Field {@link BaseArrayAdapter} for repository search.
 *
 * @author Martin Zeitler
 */
public class SortFieldSearchAdapter extends BaseArrayAdapter {
    public SortFieldSearchAdapter(@NonNull Context context) {
        super(context);
        this.setItems(context, R.array.search_sort_field_keys, R.array.search_sort_field_values);
    }
}
