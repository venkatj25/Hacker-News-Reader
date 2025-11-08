package com.jv.hnreader.ui.screens.comments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jv.hnreader.R;
import com.jv.hnreader.models.Comment;
import com.jv.hnreader.models.Story;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int PROGRESS_BAR_VIEW_TYPE = 0;
    private final int STORY_DETAIL_VIEW_TYPE = 1;
    private final int COMMENT_VIEW_TYPE = 2;

    private boolean progressBarDisabled = false;
    private boolean commentsLoaded = false;

    private List<Comment> commentList = new ArrayList<>(20);
    private Story story;

    private View.OnClickListener onLoadMoreClickListener;
    private CommentsPresenter commentsPresenter;

    public CommentsAdapter(CommentsPresenter presenter) {
        this.commentsPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == PROGRESS_BAR_VIEW_TYPE) {
            return new CommentsAdapter.ProgressBarViewHolder(inflater.inflate(R.layout.item_progress_bar_container,
                    parent, false));
        } else if (viewType == STORY_DETAIL_VIEW_TYPE) {
            return new CommentsAdapter.StoryDetailViewHolder(inflater.inflate(R.layout.item_story_detail,
                    parent, false));
        } else {
            return new CommentsAdapter.CommentViewHolder(inflater.inflate(R.layout.item_comment,
                    parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentViewHolder) {
            bindCommentViewHolder((CommentViewHolder) holder, position);
        } else if (holder instanceof StoryDetailViewHolder) {
            bindStoryDetailViewHolder((StoryDetailViewHolder) holder);
        } else{
            bindProgressBar((ProgressBarViewHolder) holder);
        }
    }

    private void bindProgressBar(CommentsAdapter.ProgressBarViewHolder holder) {
        if (commentsLoaded) holder.loadMoreText.setText("Load More Comments");
        else if (progressBarDisabled) holder.loadMoreText.setText("No More Comments");
        else holder.loadMoreText.setText("Loading...");
        /*
        if (progressBarDisabled) {
            holder.progressBar.setVisibility(View.GONE);
            holder.noMoreText.setVisibility(View.VISIBLE);
        } else holder.progressBar.setVisibility(View.VISIBLE);*/
    }

    private void bindStoryDetailViewHolder(CommentsAdapter.StoryDetailViewHolder holder) {
        if (story.getLink() == null) holder.mStoryDetailLinkView.setVisibility(GONE);
        else {
            holder.mStoryDetailLinkView.setVisibility(View.VISIBLE);
            holder.mStoryDetailLinkView.setText(story.getLinkDomain());
        }
        if (story.getText() == null) holder.mStoryDetailBodyView.setVisibility(GONE);
        else{
            holder.mStoryDetailBodyView.setVisibility(View.VISIBLE);
            holder.mStoryDetailBodyView.setText(story.getText());
        }
        holder.mStoryDetailCommentCountView.setText(String.valueOf(story.getCommentsCount()));
        holder.mStoryDetailTitleView.setText(story.getTitle());
        holder.mStoryDetailBodyView.setText(story.getText());
        holder.mStoryDetailUserView.setText(story.getUserId());
        holder.mStoryDetailVotesView.setText(story.getScore());
        //++holder.mStoryTimeView.setText(mValues.get(position).content);
    }

    private void bindCommentViewHolder(CommentsAdapter.CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position-1);
        int nestedRepliesSize = comment.getNestedCommentsSize();
        holder.mCommentBodyView.setText(comment.getText());
        holder.mCommentUserView.setText(comment.getUserId());
        if (nestedRepliesSize > 0) holder.mCommentRepliesView.setText("Show "+ nestedRepliesSize + " replies");
        //++holder.mStoryTimeView.setText(mValues.get(position).content);
    }

    @Override
    public int getItemCount() {
        Log.e("getItemCoun cal", "" + commentList.size());
        if (story == null ) return 1;
        return commentList.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (story == null) return PROGRESS_BAR_VIEW_TYPE;

        if (position == 0) {// end of stories list
            return STORY_DETAIL_VIEW_TYPE;
        } else if (position == commentList.size()+1){
            return PROGRESS_BAR_VIEW_TYPE;
        }else return COMMENT_VIEW_TYPE;
    }

    public void addComments(List<Comment> comments) {
        commentList.addAll(comments);
        commentsLoaded = true;
        this.notifyItemRangeInserted(commentList.size()+1, comments.size());
    }

    public void addStory(Story story){
        this.story = story;
        this.notifyItemChanged(0);
    }

    public void clearComments() {
        commentList.clear();
        story = null;
        commentsLoaded = false;
        progressBarDisabled = false;
        this.notifyDataSetChanged();
    }

    public void disableProgressBar() {
        progressBarDisabled = true;
        this.notifyItemChanged(commentList.size()+1);
    }

    public void onLoadMoreClick(View view) {
        if (!progressBarDisabled) {
            commentsLoaded = false;
            this.notifyItemChanged(commentList.size()+1);
            onLoadMoreClickListener.onClick(view);
        }

    }

    public class StoryDetailViewHolder extends RecyclerView.ViewHolder {
        //final View mView;
        final TextView mStoryDetailTitleView;
        final TextView mStoryDetailLinkView;
        final TextView mStoryDetailBodyView;
        final TextView mStoryDetailVotesView;
        final TextView mStoryDetailCommentCountView;
        final TextView mStoryDetailTimeView;
        final TextView mStoryDetailUserView;

        public StoryDetailViewHolder(View view) {
            super(view);
            //mView = view;
            mStoryDetailTitleView = view.findViewById(R.id.story_detail_title_text);
            mStoryDetailLinkView = view.findViewById(R.id.story_detail_link_text);
            mStoryDetailBodyView = view.findViewById(R.id.story_detail_body_text);
            mStoryDetailVotesView = view.findViewById(R.id.story_detail_votes_text);
            mStoryDetailCommentCountView = view.findViewById(R.id.story_detail_comment_count_text);
            mStoryDetailTimeView = view.findViewById(R.id.story_detail_time_text);
            mStoryDetailUserView = view.findViewById(R.id.story_detail_user_text);
        }
    }

    public class ProgressBarViewHolder extends RecyclerView.ViewHolder {
        final ProgressBar progressBar;
        final TextView loadMoreText;

        public ProgressBarViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressBar_stories_list_end);
            loadMoreText = view.findViewById(R.id.tv_list_end_message);
            loadMoreText.setOnClickListener(CommentsAdapter.this::onLoadMoreClick);
        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        //final View mView;
        final TextView mCommentBodyView;
        final TextView mCommentUserView;
        final TextView mCommentTimeView;
        final TextView mCommentVoteView;
        final TextView mCommentRepliesView;

        public CommentViewHolder(View view) {
            super(view);
            //mView = view;
            mCommentBodyView = view.findViewById(R.id.text_comment_body);
            mCommentUserView = view.findViewById(R.id.text_comment_user);
            mCommentTimeView = view.findViewById(R.id.text_comment_time);
            mCommentVoteView = view.findViewById(R.id.text_comment_vote);
            mCommentRepliesView = view.findViewById(R.id.text_comment_replies);
        }
    }

}
