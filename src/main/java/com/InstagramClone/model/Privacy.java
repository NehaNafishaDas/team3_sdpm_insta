package com.InstagramClone.model;

public class Privacy {
	public String userId;
	public boolean isPrivate;
	
	public Privacy(String userId, boolean isPrivate){
		this.userId = userId;
		this.isPrivate= isPrivate;
	}

	public String getuserId() {
		return userId;
	}

	public void setuserId(String userId) {
		this.userId = userId;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	

}
