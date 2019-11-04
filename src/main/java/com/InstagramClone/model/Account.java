package com.InstagramClone.model;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;

public class Account {

	@JsonSerialize(using = ToStringSerializer.class)
	public ObjectId _id;
    public String username;
    public String email;
    public String password;
    public String profilepicture;
    public String bio;
    public String firstName;
    public String lastName;
    public int postCount;
	@JsonSerialize(using = ToStringSerializer.class)
	public ArrayList<ObjectId> posts;
	@JsonSerialize(using = ToStringSerializer.class)
	public ArrayList<ObjectId> followedUsers;
	@JsonSerialize(using = ToStringSerializer.class)
	public ArrayList<ObjectId> followedBy;
	@JsonSerialize(using = ToStringSerializer.class)
	public ArrayList<ObjectId> likedPosts;
    
    public Account(String username, String password) {
    	this._id = new ObjectId();
    	this.username = username;
    	this.email = "";
    	this.password = password;
    	this.profilepicture = "";
    	this.bio = "";
    	this.firstName = "";
    	this.lastName = "";
    	this.posts = new ArrayList<>();
    	this.followedUsers = new ArrayList<>();
    	this.followedBy = new ArrayList<>();
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfilepicture() {
		return profilepicture;
	}

	public void setProfilepicture(String profilepicture) {
		this.profilepicture = profilepicture;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
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

	public ArrayList<ObjectId> getFollowedBy() {
		return followedBy;
	}

	public void setFollowedBy(ArrayList<ObjectId> followedBy) {
		this.followedBy = followedBy;
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