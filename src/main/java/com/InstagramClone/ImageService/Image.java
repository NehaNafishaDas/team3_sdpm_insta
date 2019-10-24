package com.InstagramClone.ImageService;

import org.bson.types.ObjectId;

public class Image {

    private ObjectId id;
	private byte[] imageFile;
    
	public Image(byte[] imageFile) {
    	this.id = new ObjectId();
        this.imageFile = imageFile;
    }

    public Image() {
    	
    }
    
    public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
    
    public byte[] getImageFile() {
		return imageFile;
	}

	public void setImageFile(byte[] imageFile) {
		this.imageFile = imageFile;
	}
}