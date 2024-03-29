package io.syslogic.github.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.syslogic.github.Constants;
import io.syslogic.github.model.PagerState;

/**
 * {@link RecyclerView.OnScrollListener}
 *
 * @author Martin Zeitler
 */
public abstract class ScrollListener extends RecyclerView.OnScrollListener {

    private final LinearLayoutManager layoutManager;
    private static PagerState state;

    private int previousTotalItemCount = 0;

    ScrollListener(LinearLayoutManager layoutManager) {
        state = new PagerState(1);
        this.layoutManager = layoutManager;
    }

    /**
     * <p>
     * Callback method to be invoked when the RecyclerView has been scrolled.
     * This will be called after the scroll has completed.
     * </p>
     * <p>
     * This callback will also be called if visible item range changes
     * after a layout calculation. In that case, dx and dy will be 0.
     * </p>
     * {@link Constants#RECYCLERVIEW_SCROLLING_THRESHOLD} defaults to 12 items.
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
        if (! recyclerView.isInEditMode() && ! state.getIsLoading()) {
            if ((firstVisibleItem + visibleItemCount + Constants.RECYCLERVIEW_SCROLLING_THRESHOLD) >= totalItemCount ) {
                this.setIsLoading(this.onLoadPage(state.getPageNumber(), totalItemCount));
            }
        }
    }

    public abstract boolean onLoadPage(int pageNumber, int totalCount);

    /* Setters */
    public static void setPageNumber(int value) {
        state.setPageNumber(value);
    }

    void setIsLoading(boolean value) {
        state.setIsLoading(value);
    }
}
