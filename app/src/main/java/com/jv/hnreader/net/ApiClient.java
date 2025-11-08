package com.jv.hnreader.net;

import android.util.Log;

import com.jv.hnreader.models.Comment;
import com.jv.hnreader.models.Story;
import com.jv.hnreader.models.Story.StoryType;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.EnumMap;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ApiClient {
    private static ApiClient instance;
    private final OkHttpClient httpClient;
    private final EnumMap<StoryType, String> storiesUrlMap;

    private ApiClient() {
        httpClient = new OkHttpClient();
        storiesUrlMap = new EnumMap<>(StoryType.class);
        storiesUrlMap.put(StoryType.TOP_STORIES, "https://hacker-news.firebaseio.com/v0/topstories.json");
        storiesUrlMap.put(StoryType.SHOW_HN, "https://hacker-news.firebaseio.com/v0/showstories.json");
        storiesUrlMap.put(StoryType.ASK_HN, "https://hacker-news.firebaseio.com/v0/askstories.json");
        storiesUrlMap.put(StoryType.JOBS, "https://hacker-news.firebaseio.com/v0/jobstories.json");
        //storiesUrlMap.put(StoryType.BEST_STORIES, "https://hacker-news.firebaseio.com/v0/beststories.json");
    }

    public static ApiClient getInstance() {
        if (instance == null) instance = new ApiClient();
        return instance;
    }

    public List<Integer> getStoryIds(Story.StoryType storyType) throws IOException {
        Log.e("getStoriesID-start", Thread.currentThread().getName());
        String url = storiesUrlMap.get(storyType);
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException();
            InputStream inputStream = response.body().byteStream();
            return new StoryJsonReader().readStoryIds(inputStream);
        }
    }

    public Story getStory(int storyId) throws IOException, URISyntaxException {
        Log.e("getStoryThread", Thread.currentThread().getName());
        String url = "https://hacker-news.firebaseio.com/v0/item/" + storyId + ".json";
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException();
            InputStream inputStream = response.body().byteStream();
            return new StoryJsonReader().readStory(inputStream);
        }
    }

    public Comment getComment(int id) throws IOException, URISyntaxException {
        //Log.e("getCommentThread", Thread.currentThread().getName());
        String url = "https://hacker-news.firebaseio.com/v0/item/" + id + ".json";
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException();
            InputStream inputStream = response.body().byteStream();
            return new CommentJsonReader().readComment(inputStream);
        }
    }

    public List<Story> getSearchResults(String query, int page, int resultsPerPage)
            throws IOException {
        Log.e("ApiClient", "getSearchResults");
        String url = HttpUrl.parse("https://hn.algolia.com/api/v1/search").newBuilder()
                .addQueryParameter("query", query)
                .addQueryParameter("tags", "story")
                .addQueryParameter("page", String.valueOf(page))
                .addQueryParameter("hitsPerPage", String.valueOf(resultsPerPage))
                .toString();
        Log.e("ApiClient", url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException();
            InputStream inputStream = response.body().byteStream();

            return new SearchJsonReader().readSearchResults(inputStream);
        }
    }
}
