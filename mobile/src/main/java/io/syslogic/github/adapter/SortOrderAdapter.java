package io.syslogic.github.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import io.syslogic.github.R;

public class SortOrderAdapter extends BaseArrayAdapter {

    public SortOrderAdapter(@NonNull Context context) {
        super(context);
        this.setItems(R.array.sortorder_keys, R.array.sortorder_values);
    }
}
