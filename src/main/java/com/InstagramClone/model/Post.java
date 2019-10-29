package com.InstagramClone.model;

import java.util.ArrayList;
import java.util.Date;

import org.apache.tomcat.jni.Time;
import org.bson.types.ObjectId;

public class Post {
	
	public ObjectId _id;
	public ArrayList<ObjectId> imageId;
	public String account;
	public boolean isPublic; 
	public String description;
	public int likes;
	public ArrayList<String> tags;
	public Date date;
    
    public Post(ArrayList<ObjectId> images, String account, String description) {
    	this.set_id(new ObjectId());
    	this.imageId = images;
    	this.account = account;
    	this.isPublic = true;
    	this.description = description;
    	this.setDate(new Date());
    }
    
    public Post() {

    }

	public String get_id() {
		return _id.toHexString();
	}

	public void set_id(ObjectId id) {
		this._id = id;
	}

	public ArrayList<ObjectId> getImageId() {
		return imageId;
	}

	public void setImageId(ArrayList<ObjectId> imagesId) {
		this.imageId = imagesId;
	}
	
    public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
}
