package com.jv.hnreader.ui.screens.comments;

import com.jv.hnreader.models.Comment;
import com.jv.hnreader.models.Story;

import java.util.List;

public interface CommentListContract {

    interface View{
        void showComments(List<Comment> comment);
        void showStoryDetail(Story story);
        void showNoMoreComments();
    }

    interface Presenter{
        void onLoadComments(int storyPosition);
        void onLoadStoryDetail(int storyPosition);
        void onDestroy();
    }
}
