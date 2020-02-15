package com.jv.hnreader.ui.screens.search;

import android.util.Log;

import com.jv.hnreader.data.EndOfItemsException;
import com.jv.hnreader.data.SearchRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenter implements SearchContract.Presenter {

    private static final String TAG = "SearchPresenter";

    private SearchContract.View view;
    private SearchRepository searchRepository;

    private String query = "";
    private int page = 0;
    private Disposable disposable = Disposables.empty();

    SearchPresenter(SearchContract.View view) {
        this.view = view;
        this.searchRepository = SearchRepository.getInstance();
    }

    @Override
    public void onLoadStories(String query) {
        if (!this.query.equals(query)){
            this.query = query;
            page = 0;
            view.clearStories();
            disposable.dispose();
        }

        Log.e(TAG, "onSearchStories");
        disposable = searchRepository.onLoadStories(query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(list -> ++page)
                .subscribe(list -> view.showStories(list));
    }

    @Override
    public void onDestroy() {
        view = null;
        if (disposable != null) disposable.dispose();
    }
}
