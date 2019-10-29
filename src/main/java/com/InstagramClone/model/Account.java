package com.InstagramClone.model;

import java.util.ArrayList;

import org.bson.types.ObjectId;

public class Account {
	
    public ObjectId _id;
    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public int postCount;
    public ArrayList<ObjectId> posts;
	public ArrayList<ObjectId> followedUsers;
	public ArrayList<ObjectId> likedPosts;
    
    public Account(String username, String password) {
    	this._id = new ObjectId();
    	this.username = username;
    	this.password = password;
    	this.firstName = "";
    	this.lastName = "";
    	this.posts = new ArrayList<>();
    	this.followedUsers = new ArrayList<>();
    	this.likedPosts = new ArrayList<>();
    	this.postCount = 0;
    }
    
    public Account() {}

	public String get_id() {
		return _id.toHexString();
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public ArrayList<ObjectId> getFollowedUsers() {
    	return followedUsers;
    }

	public void setFollowedUsers(ArrayList<ObjectId> followedUsers) {
    	this.followedUsers = followedUsers;
    }

	public ArrayList<ObjectId> getLikedPosts() {
		return likedPosts;
	}

	public void setLikedPosts(ArrayList<ObjectId> likedPosts) {
		this.likedPosts = likedPosts;
	}

	public ArrayList<ObjectId> getPosts() {
		return posts;
	}

	public void setPosts(ArrayList<ObjectId> posts) {
		this.posts = posts;
	}

	public int getPostCount() {
		return postCount;
	}

	public void setPostCount(int postCount) {
		this.postCount = postCount;
	}
}