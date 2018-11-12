package io.syslogic.github.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import io.syslogic.github.R;
import io.syslogic.github.model.SpinnerItem;

public class TopicsAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<SpinnerItem> mItems;
    private LayoutInflater layoutInflater;

    public TopicsAdapter(@NonNull Context context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.setContext(context);
        this.setItems();
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
        textView.setText(mItems.get(position).getName());
        textView.setAllCaps(true);
        return convertView;
    }

    /** Setters */
    private void setContext(@NonNull Context context) {
        this.mContext = context;
    }

    private void setItems() {
        if (this.mItems != null) {this.mItems.clear();}
        else {this.mItems = new ArrayList<>();}
        String[] topics = this.mContext.getResources().getStringArray(R.array.topics);
        String[] queryStrings = this.mContext.getResources().getStringArray(R.array.queryStrings);
        for(int i = 0; i < topics.length; i++) {
            this.mItems.add(i, new SpinnerItem(i + 1, topics[i], queryStrings[i]));
        }
    }
}
