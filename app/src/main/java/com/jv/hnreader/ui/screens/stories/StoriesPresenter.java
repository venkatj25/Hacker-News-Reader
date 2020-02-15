package com.jv.hnreader.ui.screens.stories;

import android.util.Log;

import com.jv.hnreader.data.EndOfItemsException;
import com.jv.hnreader.data.StoryRepository;
import com.jv.hnreader.datamodels.Story.StoryType;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StoriesPresenter implements StoryListContract.Presenter {

    private static final String TAG = "StoriesPresenter";

    private StoryListContract.View view;
    private StoryType storyType;
    private StoryRepository storyRepository;

    private Disposable disposable;
    private int endPosition = 0;

    StoriesPresenter(StoryListContract.View view, StoryType storyType) {
        storyRepository = StoryRepository.getInstance();
        this.storyType = storyType;
        this.view = view;
    }

    public void onLoadStories() {
        Log.e(TAG, "onLoadStories");
        disposable = storyRepository.loadStories(storyType, endPosition, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(list -> endPosition += list.size())
                .subscribe(list -> view.showStories(list), t -> {
                    if (t instanceof EndOfItemsException) {
                        view.showNoMoreStories();
                    }
                });
    }

    @Override
    public void onChangeOfStoryType(StoryType storyType) {
        if (this.storyType != storyType) {
            this.storyType = storyType;
            view.clearStories();
            endPosition = 0;
            disposable.dispose();
            this.onLoadStories();
        }
    }

    @Override
    public void onDestroy() {
        view = null;
        if (disposable != null) disposable.dispose();
    }

    StoryType getStoryType() {
        return storyType;
    }
}
