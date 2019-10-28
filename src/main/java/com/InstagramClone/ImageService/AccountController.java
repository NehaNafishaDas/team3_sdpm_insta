package com.InstagramClone.ImageService;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.InstagramClone.model.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AccountController {
    private final ImageStorageService imageStorageService = new ImageStorageService();
	private ObjectMapper om = new ObjectMapper();
	
	// Creates account given a username and password
    @PostMapping(value = "/signup", produces = "application/json")
    public Account signUp(@RequestParam String username, @RequestParam String password) {
    	DatabaseController db = DatabaseController.getInstance();
    	Account account = new Account(username, password);
    	db.createAccount(account);
    	return account;
    }

    @PostMapping(value = "/signin", produces = "application/json")
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
    
    @PostMapping(value = "/signout", produces = "application/json")
    public String signOut(HttpSession session) throws JsonProcessingException {
        session.setAttribute("loggedInAs", null);
        ObjectNode response = om.createObjectNode();
        response.put("status", "loggedout");
		return om.writeValueAsString(response);
    }
    
    // Check current login status
    @GetMapping(value = "/loginstatus", produces = "application/json")
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

    @PostMapping(value = "/blockuser/{account:.+}", produces = "application/json")
    public @ResponseBody String blockUser(@PathVariable String account) {
        return null;
    }

    @PostMapping(value = "/unblockuser/{account:.+}", produces = "application/json")
    public @ResponseBody String unblockUser(@PathVariable String account) {
        return null;
    }

    @PostMapping(value = "/changepassword", produces = "application/json")
    public String changePassword(@RequestParam String account, @RequestParam String oldPassword, @RequestParam String oldPasswordConfirmation, @RequestParam String newPassword) {
        return null;
    }

    @GetMapping(value = "/account/{account:.+}", produces = "application/json")
    public @ResponseBody String getAccount(@PathVariable String account) throws IOException {
        ObjectNode accountInfo = om.createObjectNode();
        ObjectId accId;
        try {
            accId = new ObjectId(account);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid user account");
        }
        Account requestedAccount = imageStorageService.getAccount(accId);
        accountInfo.put("_id", requestedAccount.get_id());
        accountInfo.put("username", requestedAccount.getUsername());
        accountInfo.put("firstName", requestedAccount.getFirstName());
        accountInfo.put("lastName", requestedAccount.getLastName());

        return om.writeValueAsString(accountInfo);
    }
    
    @GetMapping(value = "/accountposts/{account:.+}", produces = "application/json")
    public @ResponseBody String getAccountImages(@PathVariable String account) {
        return imageStorageService.getPostsFromAccount(account);
    }
}