package com.jv.hnreader.ui.screens.search;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jv.hnreader.R;
import com.jv.hnreader.models.Story;

import java.util.List;

public class SearchFragment extends Fragment implements SearchContract.View {

    private static final String TAG = "SearchFragment";
    private static final String ARG_QUERY = "query";
    private static final String BUNDLE_QUERY_STRING = "story_list_fragment.story_type";

    private SearchPresenter mSearchPresenter;
    private SearchAdapter mSearchAdapter;

    private SearchView mSearchView;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);/*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else { /*
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");*
        }*/
    }

    public static SearchFragment newInstance(String query) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        //mQueryString = (args != null) ? args.getString(ARG_QUERY) : null;
        mSearchPresenter = new SearchPresenter(this);
        mSearchAdapter = new SearchAdapter(getActivity(),
                () -> mSearchPresenter.onLoadStories(mSearchView.getQuery().toString()));
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated");
        Toolbar toolbar = view.findViewById(R.id.toolbar_search);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        mSearchView = toolbar.findViewById(R.id.search_view);
        mSearchView.onActionViewExpanded();
        mSearchView.setOnQueryTextListener(new SearchFragment.QueryTextListener(mSearchPresenter));

        RecyclerView mStoryRecyclerView = view.findViewById(R.id.recycler_view_search);
        mStoryRecyclerView.setAdapter(mSearchAdapter);

        //if (mQueryString != null) mSearchPresenter.onLoadStories(mQueryString);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState");
        /*outState.putParcelable(BUNDLE_STORIES_SCROLL_POSITION, storyRecyclerView.getLayoutManager()
                .onSaveInstanceState()); */
        //outState.putString(BUNDLE_QUERY_STRING, mQueryString);
    }

    @Override
    public void onDetach() {
        Log.e(TAG, "onDetach");
        super.onDetach();
        mSearchPresenter.onDestroy();
        //mListener = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                //mSearchClickListener.onSearchClick();
                return true;
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchMenuItem.expandActionView();
    }

    @Override
    public void showStories(List<Story> stories) {
        mSearchAdapter.addStories(stories);
    }

    @Override
    public void clearStories() {
        mSearchAdapter.clearStories();
    }

    @Override
    public void showNoMoreStories() {
        mSearchAdapter.disableProgressBar();
    }

    public static class QueryTextListener implements SearchView.OnQueryTextListener {
        private SearchPresenter mSearchPresenter;

        public QueryTextListener(SearchPresenter searchPresenter) {
            mSearchPresenter = searchPresenter;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            mSearchPresenter.onLoadStories(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }

}
