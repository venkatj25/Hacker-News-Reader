package com.jv.hnreader.ui.screens.comments;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.jv.hnreader.R;
import com.jv.hnreader.models.Comment;
import com.jv.hnreader.models.Story;

import java.util.List;

public class CommentsView implements CommentListContract.View {
    RecyclerView commentRecyclerView;
    CommentsPresenter commentsPresenter;

    public CommentsView(AppCompatActivity activity, Bundle savedInstanceState) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar_comment_list);
        toolbar.setTitle("Comments");

        commentRecyclerView = activity.findViewById(R.id.recycler_view_comments);
        commentRecyclerView.setAdapter(new CommentsAdapter(commentsPresenter));
        Log.e("CommentsView called", "true");
    }

    @Override
    public void showComments(List<Comment> comments) {
        Log.e("showCommentsCalled", comments.size() + "");
        ((CommentsAdapter) commentRecyclerView.getAdapter()).addComments(comments);
    }

    @Override
    public void showStoryDetail(Story story) {
        ((CommentsAdapter) commentRecyclerView.getAdapter()).addStory(story);
    }


    public void clearComments() {
        ((CommentsAdapter) commentRecyclerView.getAdapter()).clearComments();
    }

    @Override
    public void showNoMoreComments() {
        ((CommentsAdapter) commentRecyclerView.getAdapter()).disableProgressBar();
    }
}

