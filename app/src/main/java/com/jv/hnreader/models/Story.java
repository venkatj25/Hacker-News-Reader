package com.jv.hnreader.models;

import java.util.List;

public class Story {
    private String storyId;
    private String score;
    private String title;
    private String commentsCount;
    private String link;
    private String time;
    private String userId;
    private String linkDomain;
    private String text;
    private List<Integer> commentIds;

    public enum StoryType{TOP_STORIES, SHOW_HN, ASK_HN, JOBS };

    public Story(String storyId, String score, String title, String commentsCount, String link,
                 String time, String userId, String linkDomain, String text, List<Integer> commentIds) {
        this.storyId = storyId;
        this.score = score;
        this.title = title;
        this.commentsCount = commentsCount;
        this.link = link;
        this.time = time;
        this.userId = userId;
        this.linkDomain = linkDomain;
        this.text = text;
        this.commentIds = commentIds;
    }

    public String getStoryId() {
        return storyId;
    }

    public String getScore() {
        return score;
    }

    public String getTitle() {
        return title;
    }

    public String getCommentsCount() {
        return commentsCount;
    }

    public String getLink() {
        return link;
    }

    public String getTime() {
        return time;
    }

    public String getUserId() {
        return userId;
    }

    public String getLinkDomain() {
        return linkDomain;
    }

    public String getText() {
        return text;
    }

    public List<Integer> getCommentIds(int from, int count){
        // throw exception if requested initial position is greater than comments count or less than zero
        int commentsSize = commentIds.size();
        if (from < 0 || from >= commentsSize){
            throw new IndexOutOfBoundsException();
        }

        if (from + count > commentsSize){
            count = commentsSize - from;
        }

        return commentIds.subList(from, from+count);
    }
}
