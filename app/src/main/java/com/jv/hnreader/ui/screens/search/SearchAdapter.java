package com.jv.hnreader.ui.screens.search;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.recyclerview.widget.RecyclerView;

import com.jv.hnreader.R;
import com.jv.hnreader.datamodels.Story;
import com.jv.hnreader.ui.interfaces.DataLoader;
import com.jv.hnreader.ui.interfaces.OnLoadDataListener;
import com.jv.hnreader.ui.screens.stories.StoriesAdapter;

public class SearchAdapter extends StoriesAdapter {

    SearchAdapter(Context context, OnLoadDataListener onLoadDataListener) {
       super(context, onLoadDataListener);
    }
}
