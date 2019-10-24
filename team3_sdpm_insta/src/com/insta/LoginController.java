package com.insta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.insta.DB.Connection;
import com.insta.model.Credentials;
import com.insta.model.Users;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

@Controller
public class LoginController {
	
	DB database = Connection.getConnection();
	
	@RequestMapping("/") 
    public String showLogin() {
        return "login";
    }
	
	@RequestMapping("/home_page") 
    public String authenticate(Credentials credentials) {
		String userName=credentials.getUserName();
		String password=credentials.getPassword();
		BasicDBObject keys = new BasicDBObject();
		keys.put("userName",userName);
		keys.append(",Password", password);
		DBCollection data = database.getCollection("Credentials");
		DBCursor cursor = data.find(new BasicDBObject(), keys);
		if(cursor.length()==0) {
			credentials.getErr().replace("", "Username and password doesn't exist.");
			return "login";
		} else {
			credentials.setErr("");
			return "home_page";
		}
		
    }
	
	@RequestMapping("/signUp") 
    public String signUp() {
		return "signUp";
		
	}
	
//	@RequestMapping(value="/addUsers", method = RequestMethod.POST) 
//    public String addingNewUsers(@ModelAttribute("SpringWeb") Users users) {
//		System.out.println(users.getFirstName());
////		Collection<Users> users = (Collection<Users>) database.getCollection("Users");
////		if(CollectionUtils.isEmpty(users)) {
////			MongoOperations mongoOps = new MongoTemplate(MongoClients.create(), "Users");
////		    mongoOps.insert(new Users("neha","Neha123","abc",2755756869L,"neha@gmail.com"));
////		}
////		for(Users user: users) {
////			user.getFirstName();
////		}
//        return "signUp";
//    }

}
