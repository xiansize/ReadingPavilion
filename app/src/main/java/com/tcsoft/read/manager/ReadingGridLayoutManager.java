package com.tcsoft.read.manager;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by xiansize on 2017/12/11.
 */
public class ReadingGridLayoutManager extends StaggeredGridLayoutManager{

    private boolean isCanScroll = false;


    public ReadingGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }


    public void setCanScroll(boolean canScroll) {
        isCanScroll = canScroll;
    }

    @Override
    public boolean canScrollHorizontally() {
        return isCanScroll && super.canScrollHorizontally();
    }

//    @Override
//    public boolean canScrollVertically() {
//        return isCanScroll &&super.canScrollVertically();
//    }
}
