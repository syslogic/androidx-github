package io.syslogic.github.model;

import androidx.annotation.Nullable;
import androidx.databinding.Bindable;

/**
 * Model: Pager State
 * @author Martin Zeitler
 */
public class PagerState extends BaseModel {

    private long itemCount = 0L;

    private int itemsPerPage = 30;

    private int pageCount = 0;

    private int pageNumber = 1;

    private String queryString;

    private boolean isLoading = true;

    private boolean isOffline = false;

    public PagerState() {}

    public PagerState(int pageNumber) {
        this.setPageNumber(pageNumber);
    }

    public void setItemCount(long value) {
        this.itemCount = value;
    }

    public void setItemsPerPage(int value) {
        this.itemsPerPage = value;
    }

    public void setPageCount(int value) {
        this.pageCount = value;
    }

    public void setPageNumber(int value) {
        this.pageNumber = value;
    }

    public void setQueryString(String value) {
        this.queryString = value;
    }

    public void setIsLoading(boolean value) {
        this.isLoading = value;
    }

    public void setIsOffline(boolean value) {
        this.isOffline = value;
    }

    @Bindable
    public long getItemCount() {
        return this.itemCount;
    }

    @Bindable
    public int getItemsPerPage() {
        return this.itemsPerPage;
    }

    @Bindable
    public int getPageCount() {
        return this.pageCount;
    }

    @Bindable
    public int getPageNumber() {
        return this.pageNumber;
    }

    @Bindable
    public String getQueryString() {
        return this.queryString;
    }

    @Bindable
    public boolean getIsLoading() {
        return this.isLoading;
    }

    @Bindable
    public boolean getIsOffline() {
        return this.isOffline;
    }
}
