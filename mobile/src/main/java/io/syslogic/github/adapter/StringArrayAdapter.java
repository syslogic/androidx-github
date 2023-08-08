package io.syslogic.github.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import io.syslogic.github.model.SpinnerItem;

/**
 * String[] {@link BaseArrayAdapter}
 *
 * @author Martin Zeitler
 */
public class StringArrayAdapter extends BaseArrayAdapter {
    public StringArrayAdapter(@NonNull Context context, @NonNull String[] array) {
        super(context);
        for (int i = 0; i < array.length; i++) {
            this.mItems.add(i, new SpinnerItem(i + 1, array[i], array[i]));
        }
    }
}
