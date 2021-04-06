package io.syslogic.github.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import io.syslogic.github.R;

/**
 * Sort-Field Adapter
 * @author Martin Zeitler
 */
public class SortFieldAdapter extends BaseArrayAdapter {

    public SortFieldAdapter(@NonNull Context context) {
        super(context);
        this.setItems(context, R.array.sortfield_keys, R.array.sortfield_values);
    }
}
