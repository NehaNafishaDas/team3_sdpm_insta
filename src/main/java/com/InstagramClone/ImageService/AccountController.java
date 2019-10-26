package com.InstagramClone.ImageService;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.InstagramClone.models.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class AccountController {
    private final ImageStorageService imageStorageService = new ImageStorageService();
	private ObjectMapper om = new ObjectMapper();;
	
	// Creates account given a username and password
    @PostMapping("/signup")
    public Account signUp(@RequestParam String username, @RequestParam String password) {
    	DatabaseController db = DatabaseController.getInstance();
    	Account account = new Account(username, password);
    	db.createAccount(account);
    	return account;
    }
    
    // 
    @PostMapping("/signin")
    public String signIn(@RequestParam String username, @RequestParam String password, HttpSession session) throws JsonProcessingException {
    	DatabaseController db = DatabaseController.getInstance();
        String loggedInAs = (String) session.getAttribute("loggedInAs");
        
        ObjectNode response = om.createObjectNode();
        
        if(loggedInAs == null) {
        	loggedInAs = db.checkAccount(username, password);
        	if(loggedInAs != null) {
        		session.setAttribute("loggedInAs", loggedInAs);
        		response.put("status", "success");
        		response.put("loggedInAs", loggedInAs);
        		return om.writeValueAsString(response);
        	} else {
        		session.setAttribute("loggedInAs", null);
        		response.put("status", "failed");
        		return om.writeValueAsString(response);
        	}
        }
		return loggedInAs;
    }
    
    @PostMapping("/signout")
    public String signOut(HttpSession session) throws JsonProcessingException {
        session.setAttribute("loggedInAs", null);
        ObjectNode response = om.createObjectNode();
        response.put("status", "loggedout");
		return om.writeValueAsString(response);
    }
    
    // Check current login status
    @GetMapping("/loginstatus")
    public String checkLogin(HttpSession session) throws IOException {
    	String loggedInAs = (String) session.getAttribute("loggedInAs");
    	ObjectNode response = om.createObjectNode();
    	if(loggedInAs != null) {
    		response.put("status", "loggedin");
    		response.put("loggedInAs", loggedInAs);
    		return om.writeValueAsString(response);
    	} else {
    		response.put("status", "notloggedin");
    		return om.writeValueAsString(response);
    	}
    }
    
    @GetMapping("/account/{account:.+}")
    public @ResponseBody Account getAccount(@PathVariable String account) throws IOException {
        return imageStorageService.getAccount(new ObjectId(account));
    }
    
    @GetMapping("/accountposts/{account:.+}")
    public @ResponseBody String getAccountImages(@PathVariable String account) throws IOException {
        return imageStorageService.getPostsFromAccount(account);
    }
}