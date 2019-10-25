package com.InstagramClone.ImageService;

import org.bson.types.ObjectId;

public class Account {
	
    private ObjectId id;
    private String username;
    private String password;
	private String firstName;
	private String lastName;
    
    public Account(String username, String password) {
    	this.id = new ObjectId();
    	this.username = username;
    	this.password = password;
    	this.firstName = "";
    	this.lastName = "";
    }

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
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