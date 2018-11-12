package io.syslogic.github.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import io.syslogic.github.R;

public class SortFieldsAdapter extends BaseArrayAdapter {

    public SortFieldsAdapter(@NonNull Context context) {
        super(context);
        this.setItems(R.array.sortfield_keys, R.array.sortfield_values);
    }
}
