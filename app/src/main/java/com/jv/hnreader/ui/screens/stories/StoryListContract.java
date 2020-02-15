package com.jv.hnreader.ui.screens.stories;

import com.jv.hnreader.datamodels.Story;

import java.util.List;

public interface StoryListContract {

    interface View{
        void showStories(List<Story> stories);
        void clearStories();
        void showNoMoreStories();
    }

    interface Presenter{
        void onLoadStories();
        void onChangeOfStoryType(Story.StoryType storyType);
        void onDestroy();

    }
}
