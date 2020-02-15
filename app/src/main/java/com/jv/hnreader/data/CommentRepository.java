package com.jv.hnreader.data;

import com.jv.hnreader.datamodels.Comment;
import com.jv.hnreader.net.ApiClient;

import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class CommentRepository {
    private static CommentRepository instance;
    private final StoryRepository storyRepository;
    private final ApiClient apiClient;

    private List<Comment> commentList = new ArrayList<>(50);
    private int storyId;

    public static CommentRepository getInstance() {
        if (instance == null) instance = new CommentRepository();
        return instance;
    }

    private CommentRepository() {
        apiClient = ApiClient.getInstance();
        storyRepository = StoryRepository.getInstance();
        //RxJavaPlugins.setErrorHandler(e -> {if (e instanceof InterruptedIOException) e.printStackTrace();});
    }

    public Single<List<Comment>> loadComments(int storyPosition, int fromIndex, int count){

        return Single.fromCallable(() -> storyRepository.getStory(storyPosition))
                .doOnSuccess(story -> storyId = Integer.parseInt(story.getStoryId()))
                .map(story -> story.getCommentIds(fromIndex, count))
                .flattenAsObservable(list -> list)
                .flatMap(id -> Observable.fromCallable(() -> apiClient.getComment(id))
                        .subscribeOn(Schedulers.io()), 8)
                .toList()
                .doOnSuccess(comments -> commentList.addAll(comments));
    }

}
