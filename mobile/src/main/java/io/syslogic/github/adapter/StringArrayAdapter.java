package io.syslogic.github.adapter;

import android.content.Context;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import io.syslogic.github.model.SpinnerItem;


/**
 * String Array Adapter
 * @author Martin Zeitler
 * @version 1.0.0
**/
public class StringArrayAdapter extends BaseArrayAdapter {

    ArrayList<SpinnerItem> mItems;

    public StringArrayAdapter(@NonNull Context context) {
        super(context);
    }

    void setItems(@NonNull Context context, String[] array) {
        this.clearItems();
        for(int i = 0; i < array.length; i++) {
            this.mItems.add(i, new SpinnerItem(i + 1, array[i], array[i]));
        }
    }
}
