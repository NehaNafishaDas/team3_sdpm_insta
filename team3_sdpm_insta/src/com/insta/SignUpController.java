package com.insta.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.insta.DB.Connection;
import com.insta.model.Users;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class SignUpController {
	DB database = Connection.getConnection();
	
	@RequestMapping("/signUp") 
    public String signUp() {
		return "signUp";
		
	}
	
	@RequestMapping(value="/addUsers", method = RequestMethod.POST) 
    public String addingNewUsers(Users users) {
		DBCollection data = database.getCollection("Users");
		String userName=users.getUserName();
		String password=users.getPassword();
		long mobileNum = users.getMobileNum();
		String email = users.getEmail();
		String name = users.getFullName();
		BasicDBObject keys = new BasicDBObject();
		keys.put("userName",userName);
		keys.append(",Password", password);
		keys.append(",Mobile", mobileNum);
		keys.append(",email", email);
		keys.append("fullName", name);
		data.insert(new BasicDBObject(keys));
		DBCollection data2 = database.getCollection("Credentials");
		keys.put("userName",userName);
		keys.append(",Password", password);
		data2.insert(new BasicDBObject(keys));
        return "login";
   }

}
