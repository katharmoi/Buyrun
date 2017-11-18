package com.kadirkertis.orfo.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Kadir Kertis on 2.2.2017.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    int spanCount;

    public GridItemDecoration(int space, int spanCount) {
        this.space = space;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;

        outRect.bottom = space;

        //Right margin only for the last grid line elements
        if(parent.getChildLayoutPosition(view)!= 0 && parent.getChildLayoutPosition(view)%spanCount == 0){
            outRect.right = space;
        }else{
            outRect.right = 0;
        }

        //Top margin only for the first grid line elements

        if(parent.getChildLayoutPosition(view) < spanCount){
            outRect.top = space;
        }else{
            outRect.top = 0;
        }
    }
}
