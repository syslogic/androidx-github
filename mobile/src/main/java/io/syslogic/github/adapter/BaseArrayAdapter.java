package io.syslogic.github.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import io.syslogic.github.R;
import io.syslogic.github.model.SpinnerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Array {@link BaseAdapter}
 *
 * @author Martin Zeitler
 */
abstract public class BaseArrayAdapter extends BaseAdapter {

    private final LayoutInflater layoutInflater;

    @NonNull protected ArrayList<SpinnerItem> mItems = new ArrayList<>();

    BaseArrayAdapter(@NonNull Context context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.mItems.size();
    }

    @NonNull
    @Override
    public SpinnerItem getItem(int position) {
        return this.mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.mItems.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.support_simple_spinner_dropdown_item, parent, false);
        }
        convertView.setTag(this.mItems.get(position));
        AppCompatTextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(this.mItems.get(position).getName());
        textView.setAllCaps(true);
        return convertView;
    }

    void setItems(@NonNull Context context, @NonNull @ArrayRes Integer arrayKeys, @NonNull @ArrayRes Integer arrayValues) {
        this.clearItems();
        Resources res = context.getResources();
        String[] keys = res.getStringArray(arrayKeys);
        String[] values = res.getStringArray(arrayValues);
        for(int i = 0; i < keys.length; i++) {
            this.mItems.add(i, new SpinnerItem(i + 1, keys[i], values[i]));
        }
    }

    void setItems(List<SpinnerItem> items) {
        this.clearItems();
        this.mItems.addAll(items);
    }

    void clearItems() {
        this.mItems.clear();
    }
}
