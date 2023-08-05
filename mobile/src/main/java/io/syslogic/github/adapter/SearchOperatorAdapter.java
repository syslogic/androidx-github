package io.syslogic.github.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import io.syslogic.github.R;
import io.syslogic.github.api.adapter.BaseArrayAdapter;

/**
 * Search-Operator {@link BaseArrayAdapter}
 *
 * @author Martin Zeitler
 */
public class SearchOperatorAdapter extends BaseArrayAdapter {

    public SearchOperatorAdapter(@NonNull Context context) {
        super(context);
        this.setItems(context, R.array.search_operator_keys, R.array.search_operator_values);
    }
}
