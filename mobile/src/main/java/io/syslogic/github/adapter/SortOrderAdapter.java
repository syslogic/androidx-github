package io.syslogic.github.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import io.syslogic.github.R;

/**
 * Sort-Order {@link BaseArrayAdapter}
 *
 * @author Martin Zeitler
 */
public class SortOrderAdapter extends BaseArrayAdapter {

    public SortOrderAdapter(@NonNull Context context) {
        super(context);
        this.setItems(context, R.array.sort_order_keys, R.array.sort_order_values);
    }
}
