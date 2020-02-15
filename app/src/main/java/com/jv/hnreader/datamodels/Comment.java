package com.jv.hnreader.datamodels;

import java.util.Collections;
import java.util.List;

public class Comment {
    private String commentId;
    private String userId;
    private String time;
    private String text;
    private List<Integer> nestedCommentIds;
    private String parentId;

    public Comment(String commentId, String userId, String time, String text, List<Integer> nestedCommentIds,String parentId) {
        this.commentId = commentId;
        this.userId = userId;
        this.time = time;
        this.text = text;
        if (nestedCommentIds == null) this.nestedCommentIds = Collections.emptyList();
        else this.nestedCommentIds = nestedCommentIds;
        this.parentId = parentId;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getUserId() {
        return userId;
    }

    public String getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public List<Integer> getNestedCommentIds() {
        return nestedCommentIds;
    }

    public int getNestedCommentsSize(){return nestedCommentIds.size();}

    public String getParentId() {
        return parentId;
    }
}
