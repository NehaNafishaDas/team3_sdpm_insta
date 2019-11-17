package com.InstagramClone.model;

import org.bson.types.ObjectId;

import java.util.Map;

public class Image {

	public ObjectId _id;
	public Map imageFile;
    
	public Image(Map imageFile) {
    	this._id = new ObjectId();
        this.imageFile = imageFile;
    }

    public Image() {
    	
    }
    
    public String get_id() {
		return _id.toHexString();
	}

	public void set_id(ObjectId id) {
		this._id = id;
	}

	public Map getImageFile() {
		return imageFile;
	}

	public void setImageFile(Map imageFile) {
		this.imageFile = imageFile;
	}
}