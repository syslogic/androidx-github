package io.syslogic.github.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.syslogic.github.model.PagerState;

/**
 * RecyclerView Scroll-Listener
 * @author Martin Zeitler
 */
public abstract class ScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;

    private int visibleThreshold = 12;

    private int previousTotalItemCount = 0;

    private static PagerState state = new PagerState(1);

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
     * @param scrollX The amount of horizontal scroll.
     * @param scrollY The amount of vertical scroll.
    **/
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int scrollX, int scrollY) {

        super.onScrolled(recyclerView, scrollX, scrollY);

        int totalItemCount   = layoutManager.getItemCount();
        int visibleItemCount = layoutManager.getChildCount();
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

        if (totalItemCount < previousTotalItemCount) {
            if (totalItemCount == 0) {this.setIsLoading(true);}
            this.previousTotalItemCount = totalItemCount;
        }

        if (state.getIsLoading() && (totalItemCount > previousTotalItemCount)) {
            state.setPageNumber(state.getPageNumber() + 1);
            this.previousTotalItemCount = totalItemCount;
            this.setIsLoading(false);
        }

        if (!recyclerView.isInEditMode() && !state.getIsLoading() && (firstVisibleItem + visibleItemCount + visibleThreshold) >= totalItemCount ) {
            this.setIsLoading(this.onLoadPage(state.getPageNumber(), totalItemCount));
        }
    }

    public abstract boolean onLoadPage(int pageNumber, int totalCount);

    /* setters */
    public static void setPageNumber(int value) {
        state.setPageNumber(value);
    }

    void setIsLoading(boolean value) {
        state.setIsLoading(value);
    }
}
