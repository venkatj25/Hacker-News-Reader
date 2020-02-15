package com.jv.hnreader.ui.screens.search;

import com.jv.hnreader.datamodels.Story;

import java.util.List;

public interface SearchContract {

    interface View{
        void showStories(List<Story> stories);
        void clearStories();
        void showNoMoreStories();
    }

    interface Presenter {
        void onLoadStories(String query);
        void onDestroy();
    }
}
