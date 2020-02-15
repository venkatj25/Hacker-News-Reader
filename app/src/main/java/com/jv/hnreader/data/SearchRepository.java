package com.jv.hnreader.data;

import com.jv.hnreader.datamodels.Story;
import com.jv.hnreader.net.ApiClient;

import java.io.InterruptedIOException;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.plugins.RxJavaPlugins;

public class SearchRepository {
    private static final int RESULTS_PER_PAGE = 30;
    private static SearchRepository instance;
    private final ApiClient apiClient;
    private List<Story> storyList;
    private String query;

    public static SearchRepository getInstance() {
        if (instance == null) instance = new SearchRepository();
        return instance;
    }

    private SearchRepository() {
        apiClient = ApiClient.getInstance();
    }

    public Single<List<Story>> onLoadStories(String query, int page) {
        return Single.fromCallable(() -> apiClient.getSearchResults(query, page, RESULTS_PER_PAGE));

    }
}
