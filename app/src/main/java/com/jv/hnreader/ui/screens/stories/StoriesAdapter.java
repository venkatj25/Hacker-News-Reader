package com.jv.hnreader.ui.screens.stories;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jv.hnreader.R;
import com.jv.hnreader.models.Story;
import com.jv.hnreader.ui.interfaces.OnLoadDataListener;

import java.util.ArrayList;
import java.util.List;

//TODO Implement spinner
//TODO Check stuttering on initial creation of items
//TODO Implement story time
//TODO Show dot before user text
public class StoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int PROGRESS_BAR_VIEW_TYPE = 0;
    private static final int STORY_VIEW_TYPE = 1;

    private LayoutInflater mLayoutInflater;
    private OnLoadDataListener mLoadStoriesListener;

    private int mSelectedItemBackgroundColor;
    private int mSelectedItemPosition = -1;

    private boolean mLoading = true;
    private boolean mNoMoreStories = false;

    private List<Story> stories = new ArrayList<>();

    public StoriesAdapter(Context context, OnLoadDataListener onLoadStoriesListener) {
        mLayoutInflater = LayoutInflater.from(context);
        mSelectedItemBackgroundColor = context.getResources().getColor(R.color.selected_item_color);
        mLoadStoriesListener = onLoadStoriesListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("storiesadapter", "oncreateviewholder");
        if (viewType == STORY_VIEW_TYPE) {
            View view = mLayoutInflater.inflate(R.layout.item_story, parent, false);
            return new StoriesAdapter.StoryViewHolder(view, this);
        } else {
            View view = mLayoutInflater.inflate(R.layout.item_progress_bar_container, parent, false);
            return new StoriesAdapter.ProgressBarViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StoriesAdapter.StoryViewHolder) {
            Story story = stories.get(position);
            ((StoriesAdapter.StoryViewHolder) holder).bind(story, position);
        } else if (holder instanceof StoriesAdapter.ProgressBarViewHolder) {
            ((StoriesAdapter.ProgressBarViewHolder) holder).bind();
        }
    }

    @Override
    public int getItemCount() {
        Log.e("getItemCount called", "" + stories.size());
        return stories.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == stories.size()) {// end of stories list
            return PROGRESS_BAR_VIEW_TYPE;
        } else return STORY_VIEW_TYPE;
    }

    public void addStories(List<Story> storyList) {
        stories.addAll(storyList);
        mLoading = false;
        this.notifyItemRangeInserted(stories.size(), storyList.size());
    }

    public void clearStories() {
        stories.clear();
        mLoading = true;
        mNoMoreStories = false;
        mSelectedItemPosition = -1;
        this.notifyDataSetChanged();
    }

    public void disableProgressBar() {
        mNoMoreStories = true;
        this.notifyItemChanged(stories.size());
    }

    public class ProgressBarViewHolder extends RecyclerView.ViewHolder {
        final ProgressBar mProgressBar;
        final TextView mMessage;

        private ProgressBarViewHolder(View view) {
            super(view);
            mProgressBar = view.findViewById(R.id.progressBar_stories_list_end);
            mMessage = view.findViewById(R.id.tv_list_end_message);
        }

        void bind() {
            Log.e("ProgressBar bind", "true");
            if (mNoMoreStories){
                mProgressBar.setVisibility(View.GONE);
                mMessage.setText(R.string.no_more_stories);
                mMessage.setVisibility(View.VISIBLE);
            }
            else if (mLoading){
                mMessage.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
            }else{
                mLoading = true;
                mProgressBar.setVisibility(View.VISIBLE);
                mMessage.setVisibility(View.GONE);
                mLoadStoriesListener.onLoadData();
            }
            /*
            if (storiesLoaded) loadMoreText.setText("Load More Stories");
            else if (progressBarDisabled) loadMoreText.setText("No More Stories");
            else loadMoreText.setText("Loading...");*/
        }
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final View mView;
        final StoriesAdapter mAdapter;

        final TextView mCommentsCountView;
        final TextView mStoryTitleView;
        final TextView mStoryLinkView;
        final TextView mStoryUserView;
        final TextView mStoryTimeView;
        final TextView mStoryVotesView;

        StoryViewHolder(View view, StoriesAdapter adapter) {
            super(view);
            mView = view;
            mView.setOnClickListener(this);
            mAdapter = adapter;
            mCommentsCountView = view.findViewById(R.id.comment_count_text);
            mStoryTitleView = view.findViewById(R.id.story_title_text);
            mStoryLinkView = view.findViewById(R.id.story_link_text);
            mStoryVotesView = view.findViewById(R.id.story_votes_text);
            mStoryUserView = view.findViewById(R.id.story_user_text);
            mStoryTimeView = view.findViewById(R.id.story_time_text);
        }

        void bind(Story story, int position) {
            if (position == mAdapter.mSelectedItemPosition) mView.setBackgroundColor(mAdapter.mSelectedItemBackgroundColor);
            else mView.setBackgroundColor(Color.TRANSPARENT);

            if (story.getLink() == null) mStoryLinkView.setVisibility(View.GONE);
            else {
                mStoryLinkView.setVisibility(View.VISIBLE);
                mStoryLinkView.setText(story.getLinkDomain());
            }
            mCommentsCountView.setText(story.getCommentsCount());
            mStoryTitleView.setText(story.getTitle());
            mStoryUserView.setText(story.getUserId());
            mStoryVotesView.setText(story.getScore());
        }


        @Override
        public void onClick(View v) {
            Log.e("on click story", "true");
            int position = getLayoutPosition();
            int selectedItemPosition = mAdapter.mSelectedItemPosition;
            if (position != selectedItemPosition) {
                mAdapter.notifyItemChanged(selectedItemPosition);
                mView.setBackgroundColor(mAdapter.mSelectedItemBackgroundColor);
                mAdapter.mSelectedItemPosition = position;
                mStoryTimeView.setText(String.valueOf(position));
                //+storiesPresenter.onStorySelected(position);
            }
        }
    }

}
