package com.wacom.skomra.inkdemo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


/**
 * Created by aas on 10/28/16.
 */

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    RecyclerView.LayoutManager mLayoutManager;
    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }
}
