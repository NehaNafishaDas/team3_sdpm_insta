package com.insta.model;

public class Users {
	
	String fullName;
	String password;
	long mobileNum;
	String email;
	String userName;
	
	public String getFullName() {
		return fullName;
	}
	public Users(String userName, String fullName, String password, long mobileNum, String email) {
		super();
		this.fullName = fullName;
		this.password = password;
		this.mobileNum = mobileNum;
		this.email = email;
		this.userName = userName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getMobileNum() {
		return mobileNum;
	}
	public void setMobileNum(long mobileNum) {
		this.mobileNum = mobileNum;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	  public String toString() {
	    return "Users [userName=" + userName + ", fullName=" + fullName + ", Password=" + password + ", MobileNumber=" + mobileNum + ", email=" + email +"]";
	  }

}
