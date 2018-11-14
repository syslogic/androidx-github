package io.syslogic.github.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;

public class PagerState extends BaseModel {

    private Long itemCount = 0L;

    private Integer itemsPerPage = 30;

    private Integer pageCount = 0;

    private Integer pageNumber = 1;

    private Boolean isLoading = true;

    private Boolean isOffline = false;

    public PagerState() {

    }

    public PagerState(int pageNumber) {
        this.setPageNumber(pageNumber);
    }

    public void setPagerState(@Nullable PagerState state) {

    }

    public void setItemCount(@NonNull Long value) {
        this.itemCount = value;
    }

    public void setItemsPerPage(@NonNull Integer value) {
        this.itemsPerPage = value;
    }

    public void setPageCount(@NonNull Integer value) {
        this.pageCount = value;
    }

    public void setPageNumber(@NonNull Integer value) {
        this.pageNumber = value;
    }

    public void setIsLoading(@NonNull Boolean value) {
        this.isLoading = value;
    }

    public void setIsOffline(@NonNull Boolean value) {
        this.isOffline = value;
    }

    @NonNull
    @Bindable
    public PagerState getPagerState() {
        return this;
    }

    @NonNull
    @Bindable
    public Long getItemCount() {
        return this.itemCount;
    }

    @NonNull
    @Bindable
    public Integer getItemsPerPage() {
        return this.itemsPerPage;
    }

    @NonNull
    @Bindable
    public Integer getPageCount() {
        return this.pageCount;
    }

    @NonNull
    @Bindable
    public Integer getPageNumber() {
        return this.pageNumber;
    }

    @NonNull
    @Bindable
    public Boolean getIsLoading() {
        return this.isLoading;
    }

    @NonNull
    @Bindable
    public Boolean getIsOffline() {
        return this.isOffline;
    }
}
