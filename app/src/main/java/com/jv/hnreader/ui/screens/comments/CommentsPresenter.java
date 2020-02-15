package com.jv.hnreader.ui.screens.comments;

import android.util.Log;

import com.jv.hnreader.data.CommentRepository;
import com.jv.hnreader.data.EndOfItemsException;
import com.jv.hnreader.data.StoryRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommentsPresenter implements CommentListContract.Presenter {
    private CommentListContract.View view;
    private CommentRepository commentRepository;
    private StoryRepository storyRepository;

    private int fromIndex = 0;
    private Disposable disposable;

    public CommentsPresenter(CommentListContract.View view) {
        this.view = view;
        commentRepository = CommentRepository.getInstance();
        storyRepository = StoryRepository.getInstance();
    }

    @Override
    public void onLoadComments(int storyPosition) {
        Log.e("onLoadComments", "called");
        disposable = commentRepository.loadComments(storyPosition, fromIndex, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(list -> fromIndex += list.size())
                .subscribe(list -> {if(view != null) view.showComments(list);}, t -> {
                    if (t instanceof EndOfItemsException) {
                        view.showNoMoreComments();
                    }
                });

    }

    @Override
    public void onLoadStoryDetail(int storyPosition) {
        view.showStoryDetail(storyRepository.getStory(storyPosition));
    }

    @Override
    public void onDestroy() {
        view = null;
        if (!disposable.isDisposed()) disposable.dispose();
    }
}
