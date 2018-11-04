package io.syslogic.github.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RepositoriesLinearView extends RecyclerView {

    LinearLayoutManager mLinearLayoutManager;

    RepositoriesScrollListener scrollListener;

    /** Constructor */
    public RepositoriesLinearView(@NonNull Context context) {
        super(context);
    }

    /** Constructor */
    public RepositoriesLinearView(Context context, AttributeSet attrs) {

        super(context, attrs);

        this.mLinearLayoutManager = new LinearLayoutManager(this.getContext());
        this.setLayoutManager(this.mLinearLayoutManager);

        this.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        this.scrollListener = new RepositoriesScrollListener(this.mLinearLayoutManager) {
            @Override
            public boolean onLoadPage(int pageNumber, int totalItemsCount) {
                RepositoriesAdapter adapter = ((RepositoriesAdapter) getAdapter());
                if (adapter != null) {
                    adapter.fetchPage(pageNumber);
                    return true;
                }
                return false;
            }
        };

        this.addOnScrollListener(scrollListener);
    }

    public void clearAdapter() {
        RepositoriesAdapter adapter = ((RepositoriesAdapter) getAdapter());
        if (adapter != null) {
            adapter.clearItems();
            adapter.notifyDataSetChanged();
        }
    }

    public void setQuery(String value) {
        RepositoriesAdapter adapter = ((RepositoriesAdapter) getAdapter());
        if (adapter != null) {adapter.setQuery(value);}
    }
}
