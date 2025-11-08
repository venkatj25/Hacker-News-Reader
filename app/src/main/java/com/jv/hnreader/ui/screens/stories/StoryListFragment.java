package com.jv.hnreader.ui.screens.stories;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jv.hnreader.R;
import com.jv.hnreader.models.Story;
import com.jv.hnreader.models.Story.StoryType;

import java.util.List;

public class StoryListFragment extends Fragment implements StoryListContract.View {
    private static final String TAG = "StoryListFragment";
    private static final String BUNDLE_STORY_TYPE = "story_list_fragment.story_type";

    private StoriesPresenter mStoriesPresenter;
    private StoriesAdapter mStoriesAdapter;
    private OnSearchClickListener mSearchClickListener;

    public interface OnSearchClickListener{
        void onSearchClick();
    }

    public StoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach");
        if (context instanceof OnSearchClickListener) {
            mSearchClickListener = (OnSearchClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSearchClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        StoryType storyType = StoryType.TOP_STORIES;
        if (savedInstanceState != null) {
            storyType = (Story.StoryType) savedInstanceState.getSerializable(BUNDLE_STORY_TYPE);
        }
        mStoriesPresenter = new StoriesPresenter(this, storyType);
        mStoriesAdapter = new StoriesAdapter(getActivity(), () -> mStoriesPresenter.onLoadStories());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_story_list,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated");
        Toolbar toolbar = view.findViewById(R.id.toolbar_story_list);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        RecyclerView mStoryRecyclerView = view.findViewById(R.id.story_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setItemPrefetchEnabled(true);
        layoutManager.setInitialPrefetchItemCount(8);
        mStoryRecyclerView.setLayoutManager(layoutManager);
        mStoryRecyclerView.setAdapter(mStoriesAdapter);
        mStoryRecyclerView.setHasFixedSize(true);
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationMenuClick);
        mStoriesPresenter.onLoadStories();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState");
        /*outState.putParcelable(BUNDLE_STORIES_SCROLL_POSITION, storyRecyclerView.getLayoutManager()
                .onSaveInstanceState()); */
        outState.putSerializable(BUNDLE_STORY_TYPE, mStoriesPresenter.getStoryType());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach");
        mSearchClickListener = null;
        mStoriesPresenter.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.story_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_menu_item:
                mSearchClickListener.onSearchClick();
                return true;
            case R.id.preferences_menu_item:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showStories(List<Story> stories) {
        mStoriesAdapter.addStories(stories);
    }

    @Override
    public void clearStories() {
        mStoriesAdapter.clearStories();
    }

    @Override
    public void showNoMoreStories() {
        mStoriesAdapter.disableProgressBar();
    }

    private boolean onNavigationMenuClick(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case R.id.navigation_showhn:
                mStoriesPresenter.onChangeOfStoryType(StoryType.SHOW_HN);
                return true;
            case R.id.navigation_askhn:
                mStoriesPresenter.onChangeOfStoryType(StoryType.ASK_HN);
                return true;
            case R.id.navigation_jobs:
                mStoriesPresenter.onChangeOfStoryType(StoryType.JOBS);
                return true;
            default:
                mStoriesPresenter.onChangeOfStoryType(StoryType.TOP_STORIES);
                return true;
        }
    }

}
