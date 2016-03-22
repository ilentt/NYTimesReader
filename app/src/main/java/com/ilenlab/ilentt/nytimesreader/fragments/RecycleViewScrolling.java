package com.ilenlab.ilentt.nytimesreader.fragments;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public abstract class RecycleViewScrolling extends RecyclerView.OnScrollListener {

    private int minScrollItem = 5;
    private int currentOffset = 0;
    private int preTotalItem = 0;
    private boolean loading = true;
    private int startPageIndex = 0;

    RecyclerView.LayoutManager layoutManager;

    public RecycleViewScrolling(LinearLayoutManager linearLayoutManager) {
        this.layoutManager = linearLayoutManager;
    }

    public RecycleViewScrolling(GridLayoutManager gridLayoutManager) {
        this.layoutManager = gridLayoutManager;
        minScrollItem  = minScrollItem * gridLayoutManager.getSpanCount();
    }

    public RecycleViewScrolling(StaggeredGridLayoutManager gridLayoutManager) {
        this.layoutManager = gridLayoutManager;
        minScrollItem  = minScrollItem * gridLayoutManager.getSpanCount();
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for(int i = 0; i < lastVisibleItemPositions.length; i++) {
            if(i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if(lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return  maxSize;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItem = layoutManager.getItemCount();
        if(layoutManager instanceof StaggeredGridLayoutManager) {
            int [] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if(layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if(layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        if(totalItem < preTotalItem) {
            this.currentOffset = this.startPageIndex;
            this.preTotalItem = totalItem;
            if(totalItem == 0) {
                this.loading = true;
            }
        }

        if(loading && (totalItem > preTotalItem)) {
            loading = false;
            preTotalItem = totalItem;
        }

        if(!loading && (lastVisibleItemPosition + minScrollItem) > totalItem && currentOffset < 10) {
            currentOffset++;
            onLoadMode(currentOffset, totalItem);
            loading = true;
        }
    }

    // define the process for actually loading more data base on page
    public abstract void onLoadMode(int page, int totalItem);
}