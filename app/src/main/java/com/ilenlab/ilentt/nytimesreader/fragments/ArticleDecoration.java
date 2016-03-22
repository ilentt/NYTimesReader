package com.ilenlab.ilentt.nytimesreader.fragments;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ADMIN on 3/22/2016.
 */
public class ArticleDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public ArticleDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.left = space + 5;

        if(parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }
}