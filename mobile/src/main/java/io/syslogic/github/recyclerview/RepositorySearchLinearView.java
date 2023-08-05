package io.syslogic.github.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Repositories {@link RecyclerView}
 *
 * @author Martin Zeitler
 */
public class RepositorySearchLinearView extends RecyclerView {

    LinearLayoutManager mLinearLayoutManager;
    ScrollListener scrollListener;

    /** Constructor */
    public RepositorySearchLinearView(@NonNull Context context) {
        super(context);
    }

    /** Constructor */
    public RepositorySearchLinearView(@NonNull Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        this.mLinearLayoutManager = new LinearLayoutManager(this.getContext());
        this.setLayoutManager(this.mLinearLayoutManager);

        this.scrollListener = new ScrollListener(this.mLinearLayoutManager) {
            @Override
            public boolean onLoadPage(int pageNumber, int totalItemsCount) {
                RepositorySearchAdapter adapter = ((RepositorySearchAdapter) getAdapter());
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
        RepositorySearchAdapter adapter = ((RepositorySearchAdapter) getAdapter());
        if (adapter != null) {
            adapter.clearItems();
            adapter.notifyDataSetChanged();
        }
    }

    public void setQueryString(@NonNull String value) {
        RepositorySearchAdapter adapter = ((RepositorySearchAdapter) getAdapter());
        if (adapter != null) {adapter.setQueryString(value);}
    }

    @Nullable
    public String getQueryString() {
        RepositorySearchAdapter adapter = ((RepositorySearchAdapter) getAdapter());
        if (adapter == null) {return null;}
        return adapter.getQueryString();
    }

    @NonNull
    public ScrollListener getOnScrollListener() {
        return this.scrollListener;
    }
}
