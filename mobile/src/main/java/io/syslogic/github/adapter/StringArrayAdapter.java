package io.syslogic.github.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import io.syslogic.github.api.model.SpinnerItem;

import java.util.ArrayList;

/**
 * String[] {@link BaseArrayAdapter}
 *
 * @author Martin Zeitler
 */
public class StringArrayAdapter extends BaseArrayAdapter {

    ArrayList<SpinnerItem> mItems;

    public StringArrayAdapter(@NonNull Context context) {
        super(context);
    }

    void setItems(@NonNull Context context, @NonNull String[] array) {
        this.clearItems();
        for(int i = 0; i < array.length; i++) {
            this.mItems.add(i, new SpinnerItem(i + 1, array[i], array[i]));
        }
    }
}
