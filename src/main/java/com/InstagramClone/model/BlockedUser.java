package com.InstagramClone.model;

import java.util.List;

public class BlockedUser {
	public String currentUser;
	public List<String> blockedUsers;
	
	public BlockedUser(String currentUser, List<String> blockedUsers){
		this.currentUser = currentUser;
		this.blockedUsers = blockedUsers;
	}
	
	public String getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}
	
	public List<String> getBlockedUsers() {
		return blockedUsers;
	}
	
	public void setBlockedUsers(List<String> blockedUsers) {
		this.blockedUsers = blockedUsers;
	}
	

}
