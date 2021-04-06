package io.syslogic.github.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import io.syslogic.github.R;

/**
 * Sort-Order Adapter
 * @author Martin Zeitler
 */
public class SortOrderAdapter extends BaseArrayAdapter {

    public SortOrderAdapter(@NonNull Context context) {
        super(context);
        this.setItems(context, R.array.sortorder_keys, R.array.sortorder_values);
    }
}
