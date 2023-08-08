package io.syslogic.github.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import io.syslogic.github.R;

/**
 * Sort-Field {@link BaseArrayAdapter} for repository listing.
 *
 * @author Martin Zeitler
 */
public class SortFieldListingAdapter extends BaseArrayAdapter {
    public SortFieldListingAdapter(@NonNull Context context) {
        super(context);
        this.setItems(context, R.array.listing_sort_field_keys, R.array.listing_sort_field_values);
    }
}
