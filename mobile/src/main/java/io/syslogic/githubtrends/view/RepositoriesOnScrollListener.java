package io.syslogic.githubtrends.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RepositoriesOnScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;

    private int visibleThreshold = 0;

    private int previousTotalItemCount = 0;

    private int currentPage = 1;

    private boolean loading = true;

    public RepositoriesOnScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public RepositoriesOnScrollListener(LinearLayoutManager layoutManager, int threshold) {
        this.layoutManager = layoutManager;
        this.visibleThreshold = threshold;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount   = layoutManager.getItemCount();
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

        if (totalItemCount < previousTotalItemCount) {
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {this.loading = true;}
        }

		if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage ++;
        }

		if (!loading && (firstVisibleItem + visibleItemCount + visibleThreshold) >= totalItemCount ) {
            loading = onLoadPage(currentPage, totalItemCount);
        }
    }

    public abstract boolean onLoadPage(int pageNumber, int totalCount);

    public abstract boolean isLoading();
}