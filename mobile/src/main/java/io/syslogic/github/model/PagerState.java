package io.syslogic.github.model;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

public class PagerState extends BaseModel {

    private long itemCount = 0;

    private int itemsPerPage = 30;

    private int pageCount = 0;

    private int pageNumber = 1;

    private boolean isLoading = true;

    public PagerState() {

    }

    public PagerState(int pageNumber) {
        this.setPageNumber(pageNumber);
    }

    public void setPagerState(int pageNumber) {
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

    public void setIsLoading(boolean value) {
        this.isLoading = value;
    }

    @Bindable
    public PagerState getPagerState() {
        return this;
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
    public boolean getIsLoading() {
        return this.isLoading;
    }
}
