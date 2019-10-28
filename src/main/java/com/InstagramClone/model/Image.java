package com.InstagramClone.model;

import org.bson.types.ObjectId;

public class Image {

	public ObjectId _id;
	public byte[] imageFile;
    
	public Image(byte[] imageFile) {
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
    
    public byte[] getImageFile() {
		return imageFile;
	}

	public void setImageFile(byte[] imageFile) {
		this.imageFile = imageFile;
	}
}