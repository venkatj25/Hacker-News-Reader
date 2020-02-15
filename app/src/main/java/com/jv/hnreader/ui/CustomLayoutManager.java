package com.jv.hnreader.ui;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomLayoutManager extends LinearLayoutManager {
    private static final int EXTRA_LAYOUT_SPACE = 800;
    public CustomLayoutManager(Context context) {
        super(context);
    }

    public int getExtraLayoutSpace(RecyclerView.State state){
        return EXTRA_LAYOUT_SPACE;
    }


}
