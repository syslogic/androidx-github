package io.syslogic.github.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.widget.AppCompatTextView;
import io.syslogic.github.R;
import io.syslogic.github.model.SpinnerItem;

public class LanguagesAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<SpinnerItem> mItems;
    private LayoutInflater layoutInflater;

    public LanguagesAdapter(Context context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.setContext(context);
        this.setItems();
    }

    @Override
    public int getCount() {
        return this.mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.mItems.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
    public void setContext(Context context) {
        mContext = context;
    }
    private void setItems() {

        if (this.mItems != null) {this.mItems.clear();}
        else {this.mItems = new ArrayList<>();}

        String[] names = this.mContext.getResources().getStringArray(R.array.languages);
        String[] queries = this.mContext.getResources().getStringArray(R.array.queries);
        for(int i = 0; i < queries.length; i++) {
            this.mItems.add(i, new SpinnerItem(i + 1, names[i], queries[i]));
        }
    }
}
