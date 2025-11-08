package com.jv.hnreader.ui.screens.search;

import android.content.Context;

import com.jv.hnreader.ui.interfaces.OnLoadDataListener;
import com.jv.hnreader.ui.screens.stories.StoriesAdapter;

public class SearchAdapter extends StoriesAdapter {

    SearchAdapter(Context context, OnLoadDataListener onLoadDataListener) {
       super(context, onLoadDataListener);
    }
}
