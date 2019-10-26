package com.InstagramClone.models;

import java.util.ArrayList;

import org.bson.types.ObjectId;

public class Account {
	
    public ObjectId _id;
    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public ArrayList<Post> posts;
    
    public Account(String username, String password) {
    	this._id = new ObjectId();
    	this.username = username;
    	this.password = password;
    	this.firstName = "";
    	this.lastName = "";
    	this.posts = new ArrayList<Post>();
    }
    
    public Account() {
    	
    }

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
}