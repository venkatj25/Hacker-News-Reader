package com.jv.hnreader.data;

import com.jv.hnreader.models.Story;
import com.jv.hnreader.models.Story.StoryType;
import com.jv.hnreader.net.ApiClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class StoryRepository {
    private static StoryRepository instance;
    private final ApiClient apiClient;
    //private ExecutorService executor = Executors.newFixedThreadPool(8);
    private List<Integer> topStoryIds = new ArrayList<>(500);
    private List<Integer> showStoryIds = new ArrayList<>(75);
    private List<Integer> askStoryIds = new ArrayList<>(75);
    private List<Integer> jobStoryIds = new ArrayList<>(75);

    private List<Story> topStoryList = new ArrayList<>(500);
    private List<Story> showStoryList = new ArrayList<>(75);
    private List<Story> askStoryList = new ArrayList<>(75);
    private List<Story> jobStoryList = new ArrayList<>(75);

    public static StoryRepository getInstance() {
        if (instance == null) instance = new StoryRepository();
        return instance;
    }

    private StoryRepository() {
        apiClient = ApiClient.getInstance();
        //RxJavaPlugins.setErrorHandler(e -> {if (e instanceof InterruptedIOException) e.printStackTrace();});
    }

    public Single<List<Story>> loadStories(StoryType storyType, int from, int count) {
        int endIndex = from + count; // exclusive
        int idListSize = 0 ;
        int storyListSize = 0;
        List<Story> storyList = null;
        List<Integer> storyIdList = null;
        switch (storyType){
            case TOP_STORIES:
                idListSize = topStoryIds.size();
                storyListSize = topStoryList.size();
                storyList = topStoryList;
                storyIdList = topStoryIds;
                break;
            case SHOW_HN:
                idListSize = showStoryIds.size();
                storyListSize = showStoryList.size();
                storyList = showStoryList;
                storyIdList = showStoryIds;
                break;
            case ASK_HN:
                idListSize = askStoryIds.size();
                storyListSize = askStoryList.size();
                storyList = askStoryList;
                storyIdList = askStoryIds;
                break;
            case JOBS:
                idListSize = jobStoryIds.size();
                storyListSize = jobStoryList.size();
                storyList = jobStoryList;
                storyIdList = jobStoryIds;
                break;
        }

        if (idListSize == 0) {
            List<Integer> finalStoryIdList = storyIdList;
            List<Story> finalStoryList = storyList;
            return Single.fromCallable(() -> apiClient.getStoryIds(storyType))
                    .doOnSuccess(idList -> finalStoryIdList.addAll(idList))
                    .map(list -> list.subList(from, from + count))
                    .flattenAsObservable(s -> s)
                    .flatMap(id -> Observable.fromCallable(() -> apiClient.getStory(id))
                            .subscribeOn(Schedulers.io()), 8)
                    .toList()
                    .doOnSuccess(stories -> finalStoryList.addAll(stories))
                    ;
        } else if (from >= idListSize) {
            // throw exception if idList is not empty and from index is greater than idListSize
            return Single.error(new EndOfItemsException());
        } else if (from >= storyListSize) {
            // download stories from api if topStoryList doesn't have them
            if (endIndex > idListSize) endIndex = idListSize;
            List<Story> finalStoryList2 = storyList;
            return Observable.fromIterable(storyIdList.subList(from, endIndex))
                    .flatMap(id -> Observable.fromCallable(() -> apiClient.getStory(id))
                            .subscribeOn(Schedulers.io()), 8)
                    .toList()
                    .doOnSuccess(stories -> finalStoryList2.addAll(stories))
                    ;
        } else if (endIndex >= storyListSize) {
            if (endIndex > idListSize) endIndex = idListSize;
            Observable<Story> cachedStories = Observable.fromIterable(storyList.subList(from, storyListSize));
            List<Story> finalStoryList1 = storyList;
            Observable<Story> downloadedStories = Observable
                    .fromIterable(storyIdList.subList(storyListSize, endIndex))
                    .flatMap(id -> Observable.fromCallable(() -> apiClient.getStory(id))
                            .subscribeOn(Schedulers.io()), 8)
                    .doOnNext(story -> finalStoryList1.add(story));
            return Observable.concat(cachedStories, downloadedStories).toList();
        }
        // get stories if topStoriesList has them
        List<Story> finalStoryList3 = storyList;
        return Single.fromCallable(() -> finalStoryList3.subList(from, from + count));
    }


    public int getStoriesSize(Story.StoryType storyType){
        switch (storyType){
            case TOP_STORIES:
                return topStoryList.size();
            case SHOW_HN:
                return showStoryList.size();
            case ASK_HN:
                return askStoryList.size();
            case JOBS:
                return jobStoryList.size();
            default:
                throw new IllegalArgumentException("Invalid Story type");
        }
    }

    public Story getStory(int index){
        return topStoryList.get(index);
    }

}
