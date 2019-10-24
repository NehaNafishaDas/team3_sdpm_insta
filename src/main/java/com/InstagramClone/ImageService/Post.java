package com.InstagramClone.ImageService;

import java.util.ArrayList;

import org.apache.tomcat.jni.Time;
import org.bson.types.ObjectId;

public class Post {
	
    private ObjectId id;
    private ArrayList<ObjectId> imageId;
    private String account;
	private boolean isPublic; 
    private String description;
    private long time;
    
    public Post(ArrayList<ObjectId> imageId, String account, String description) {
    	this.setId(new ObjectId());
    	this.imageId = imageId;
    	this.account = account;
    	this.isPublic = true;
    	this.description = description;
    	this.setTime(Time.now());
    }
    
    public Post() {

    }

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
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

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
