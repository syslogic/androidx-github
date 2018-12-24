package io.syslogic.github.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import io.syslogic.github.R;

/**
 * Topic Adapter
 * @author Martin Zeitler
 * @version 1.0.0
**/
public class TopicAdapter extends BaseArrayAdapter {

    public TopicAdapter(@NonNull Context context) {
        super(context);
        this.setItems(context, R.array.topic_keys, R.array.topic_values);
    }
}
