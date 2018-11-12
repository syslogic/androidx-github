package io.syslogic.github.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import io.syslogic.github.R;

public class TopicsAdapter extends BaseArrayAdapter {

    public TopicsAdapter(@NonNull Context context) {
        super(context);
        this.setItems(R.array.topic_keys, R.array.topic_values);
    }
}
