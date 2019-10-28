package com.InstagramClone.model;

import java.util.ArrayList;

import org.apache.tomcat.jni.Time;
import org.bson.types.ObjectId;

public class Post {
	
	public ObjectId _id;
	public ArrayList<String> imageId;
	public String account;
	public boolean isPublic; 
	public String description;
	public int likes;
	public long time;
    
    public Post(ArrayList<String> images, String account, String description) {
    	this.set_id(new ObjectId());
    	this.imageId = images;
    	this.account = account;
    	this.isPublic = true;
    	this.description = description;
    	this.setTime(Time.now());
    }
    
    public Post() {

    }

	public String get_id() {
		return _id.toHexString();
	}

	public void set_id(ObjectId id) {
		this._id = id;
	}

	public ArrayList<String> getImageId() {
		return imageId;
	}

	public void setImageId(ArrayList<String> imagesId) {
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

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
