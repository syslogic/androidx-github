package io.syslogic.github.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;

    private int visibleThreshold = 6;

    private int previousTotalItemCount = 0;

    private static int currentPage = 1;

    private boolean isLoading = true;

    ScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    /**
     * Callback method to be invoked when the RecyclerView has been scrolled.
     * This will be called after the scroll has completed.
     *
     * This callback will also be called if visible item range changes
     * after a layout calculation. In that case, dx and dy will be 0.
     *
     * @param recyclerView The RecyclerView which scrolled.
     * @param dx The amount of horizontal scroll.
     * @param dy The amount of vertical scroll.
    **/
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
    public static void setPageNumber(int value) {
        currentPage = value;
    }


    public abstract boolean onLoadPage(int pageNumber, int totalCount);
}