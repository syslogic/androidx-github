package io.syslogic.github.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RepositoriesScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;

    private int visibleThreshold = 10;

    private int previousTotalItemCount = 0;

    public static int currentPage = 1;

    private boolean isLoading = true;

    RepositoriesScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public RepositoriesScrollListener(LinearLayoutManager layoutManager, int threshold) {
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
            if (totalItemCount == 0) {this.isLoading = true;}
        }

		if (isLoading && (totalItemCount > previousTotalItemCount)) {
            isLoading = false;
            previousTotalItemCount = totalItemCount;
            currentPage ++;
        }

		if (! isLoading && (firstVisibleItem + visibleItemCount + visibleThreshold) >= totalItemCount ) {
            isLoading = onLoadPage(currentPage, totalItemCount);
        }
    }

    public abstract boolean onLoadPage(int pageNumber, int totalCount);
}