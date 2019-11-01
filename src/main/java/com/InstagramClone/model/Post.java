package com.InstagramClone.model;

import java.util.ArrayList;
import java.util.Date;

import org.bson.types.ObjectId;

public class Post {

	public ObjectId _id;
	public ArrayList<String> imageId;
	public ObjectId account;
	public String description;
	public int likes;
	public ArrayList<String> tags;
	public Date date;
	public ArrayList<Comment> comments;

    public Post(ArrayList<String> images, ObjectId account, String description) {
    	this.set_id(new ObjectId());
    	this.imageId = images;
    	this.account = account;
    	if(description != null) this.description = description;
    	else this.description = "";
    	this.likes = 0;
    	this.tags = new ArrayList<>();
    	this.setDate(new Date());
    	this.comments = new ArrayList<>();
    }

    public Post() {

    }

	public String get_id() {
		return _id.toHexString();
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public ArrayList<String> getImageId() {
		return imageId;
	}

	public void setImageId(ArrayList<String> imageId) {
		this.imageId = imageId;
	}

	public ObjectId getAccount() {
		return account;
	}

	public void setAccount(ObjectId account) {
		this.account = account;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public ArrayList<Comment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}
}