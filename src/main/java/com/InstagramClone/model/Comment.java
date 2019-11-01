package com.InstagramClone.model;

import java.util.Date;

public final class Comment {
    private String username;
    private String comment;
    private Date date;

    public Comment(String username, String comment) {
        this.username = username;
        this.comment = comment;
        this.date = new Date();
    }

    public Comment() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
