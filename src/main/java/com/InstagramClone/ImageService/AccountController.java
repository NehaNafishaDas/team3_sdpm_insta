package com.InstagramClone.ImageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.InstagramClone.model.Post;
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
	private ObjectMapper om = new ObjectMapper();
    private final DatabaseController db = DatabaseController.getInstance();

    // Creates account given a username and password
    @PostMapping(value = "/signup", produces = "application/json")
    public Account signUp(@RequestParam String username, @RequestParam String password) {
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
        } else {
            response.put("status", "failed");
            return om.writeValueAsString(response);
        }
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

    // TODO
    @PostMapping(value = "/blockuser/{account:.+}", produces = "application/json")
    public @ResponseBody String blockUser(@PathVariable String account) {
        return null;
    }

    // TODO
    @PostMapping(value = "/unblockuser/{account:.+}", produces = "application/json")
    public @ResponseBody String unblockUser(@PathVariable String account) {
        return null;
    }

    // TODO
    @PostMapping(value = "/changepassword", produces = "application/json")
    public String changePassword(@RequestParam String account, @RequestParam String oldPassword, @RequestParam String oldPasswordConfirmation, @RequestParam String newPassword) {
        return null;
    }

    @GetMapping(value = "/account/{account:.+}", produces = "application/json")
    public @ResponseBody String getAccount(@PathVariable String account) throws IOException {
        ObjectId accId;
        try {
            accId = new ObjectId(account);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid user account");
        }
        Account requestedAccount = db.getAccount(accId);
        if(requestedAccount == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid user account");
        }
        ObjectNode response = om.createObjectNode();
        response.put("_id", requestedAccount.get_id());
        response.put("username", requestedAccount.getUsername());
        response.put("firstName", requestedAccount.getFirstName());
        response.put("lastName", requestedAccount.getLastName());

        return om.writeValueAsString(response);
    }

    @PostMapping(value = "/makepost", produces = "application/json")
    public @ResponseBody String makePost(@RequestParam String[] images,
                                         @RequestParam String description,
                                         HttpSession session) throws JsonProcessingException {
        String loggedInAs = (String) session.getAttribute("loggedInAs");
        if(loggedInAs == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not logged in");
        }
        ArrayList<ObjectId> imageList = new ArrayList<>();
        Account a = db.getAccount(new ObjectId(loggedInAs));
        try {
            for (int i = 0; i < images.length; i++) {
                imageList.add(new ObjectId(images[i]));
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid image id");
        }
        Post p = new Post(imageList, a.get_id(), description);
        db.makePost(a, p);
        ObjectNode response = om.createObjectNode();
        response.put("_id", p.get_id());
        return om.writeValueAsString(response);
    }
    
    @GetMapping(value = "/accountposts/{account:.+}", produces = "application/json")
    public @ResponseBody String getAccountImages(@PathVariable String account) throws JsonProcessingException {
        Account a;
        try {
            a = db.getAccount(new ObjectId(account));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid user account");
        }

        ObjectNode response = om.createObjectNode();
        if(a == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no posts on account");
        }

        ArrayList<ObjectId> p = a.getPosts();

        Iterator<ObjectId> posts = p.iterator();
        int postCount = 0;
        while(posts.hasNext()) {
            response.put(String.valueOf(postCount), posts.next().toHexString());
            postCount++;
        }
        return om.writeValueAsString(response);
    }
}