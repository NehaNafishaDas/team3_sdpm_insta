package com.InstagramClone.ImageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.InstagramClone.model.Image;
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
    public String signUp(@RequestParam String username, @RequestParam String password) throws JsonProcessingException {
        Account account = new Account(username, password);
        if (db.getAccount(username) != null) {
            ObjectNode response = om.createObjectNode();
            response.put("status", "failed");
            response.put("error", "Account already exists with username " + username);
            return om.writeValueAsString(response);
        } else {
            ObjectNode response = om.createObjectNode();
            String id = db.createAccount(account);
            response.put("status", "success");
            response.put("username", username);
            response.put("_id", id);
            return om.writeValueAsString(response);
        }
    }

    @PostMapping(value = "/signin", produces = "application/json")
    public String signIn(@RequestParam String username, @RequestParam String password, HttpSession session) throws JsonProcessingException {
        DatabaseController db = DatabaseController.getInstance();
        String loggedInAs = (String) session.getAttribute("Username");

        ObjectNode response = om.createObjectNode();

        if (loggedInAs == null) {
            Account loggedInAccount = db.checkAccount(username, password);
            if (loggedInAccount != null) {
                session.setAttribute("username", loggedInAccount.getUsername());
                session.setAttribute("userid", loggedInAccount.get_id());
                response.put("status", "success");
                response.put("username", loggedInAccount.getUsername());
                return om.writeValueAsString(response);
            } else {
                session.setAttribute("Username", null);
                response.put("status", "failed");
                response.put("error", "Invalid username or password");
                return om.writeValueAsString(response);
            }
        } else {
            response.put("status", "failed");
            response.put("error", "Already logged in");
            return om.writeValueAsString(response);
        }
    }

    @PostMapping(value = "/signout", produces = "application/json")
    public String signOut(HttpSession session) throws JsonProcessingException {
        session.setAttribute("username", null);
        ObjectNode response = om.createObjectNode();
        response.put("status", "loggedout");
        return om.writeValueAsString(response);
    }

    // Check current login status
    @GetMapping(value = "/loginstatus", produces = "application/json")
    public String checkLogin(HttpSession session) throws IOException {
        String loggedInAs = (String) session.getAttribute("username");
        ObjectNode response = om.createObjectNode();
        if (loggedInAs != null) {
            response.put("status", "loggedin");
            response.put("username", loggedInAs);
            return om.writeValueAsString(response);
        } else {
            response.put("status", "notloggedin");
            return om.writeValueAsString(response);
        }
    }

    // TODO
    @PostMapping(value = "/blockuser/{account:.+}", produces = "application/json")
    public @ResponseBody
    String blockUser(@PathVariable String account) {
        return null;
    }

    // TODO
    @PostMapping(value = "/unblockuser/{account:.+}", produces = "application/json")
    public @ResponseBody
    String unblockUser(@PathVariable String account) {
        return null;
    }

    // TODO
    @PostMapping(value = "/changepassword", produces = "application/json")
    public String changePassword(@RequestParam String account,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword) {
        return null;
    }

    @PostMapping(value = "/follow", produces = "application/json")
    public String followuser(@RequestParam String targetaccount,
                             HttpSession session) throws JsonProcessingException {
        String username = (String) session.getAttribute("username");
        String userid = (String) session.getAttribute("userid");
        ObjectNode response = om.createObjectNode();
        if (username == null) {
            response.put("status", "failed");
            response.put("error", "Not logged in");
            return om.writeValueAsString(response);
        } else {
            Account followTarget = db.getAccount(targetaccount);
            Account user = db.getAccount(new ObjectId(userid));
            if (followTarget == null) {
                response.put("status", "failed");
                response.put("error", "User not found");
                return om.writeValueAsString(response);
            } else {
                db.followUser(followTarget._id, user._id);
                response.put("status", "success");
                response.put("error", "Now following user " + targetaccount);
                return om.writeValueAsString(response);
            }
        }
    }

    @PostMapping(value = "/unfollow", produces = "application/json")
    public String unfollowuser(@RequestParam String targetaccount,
                               HttpSession session) throws JsonProcessingException {
        String username = (String) session.getAttribute("username");
        String userid = (String) session.getAttribute("userid");
        ObjectNode response = om.createObjectNode();
        if (username == null) {
            response.put("status", "failed");
            response.put("error", "Not logged in");
            return om.writeValueAsString(response);
        } else {
            Account followTarget = db.getAccount(targetaccount);
            Account user = db.getAccount(new ObjectId(userid));
            if (followTarget == null) {
                response.put("status", "failed");
                response.put("error", "User not found");
                return om.writeValueAsString(response);
            } else {
                db.unfollowUser(followTarget._id, user._id);
                response.put("status", "success");
                response.put("error", "Unfollowed user " + targetaccount);
                return om.writeValueAsString(response);
            }
        }
    }

    @GetMapping(value = "/account/{account:.+}", produces = "application/json")
    public @ResponseBody
    String getAccount(@PathVariable String account) throws IOException {
        ObjectId accId;
        try {
            accId = new ObjectId(account);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid user account");
        }
        Account requestedAccount = db.getAccount(accId);
        if (requestedAccount == null) {
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
    public @ResponseBody
    String makePost(@RequestParam String[] images,
                    @RequestParam String description,
                    HttpSession session) throws JsonProcessingException {
        String loggedInAs = (String) session.getAttribute("username");
        if (loggedInAs == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not logged in");
        }
        ArrayList<String> imageList = new ArrayList<>();
        Account a = db.getAccount(new ObjectId(loggedInAs));
        try {
            for (int i = 0; i < images.length; i++) {
                imageList.add(images[i]);
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid image id");
        }
        Post p = new Post(imageList, a._id, description);
        db.insertPost(p);
        ObjectNode response = om.createObjectNode();
        response.put("_id", p.get_id().toHexString());
        return om.writeValueAsString(response);
    }

    @GetMapping(value = "/accountposts", produces = "application/json")
    public @ResponseBody
    String getAccountPosts(HttpSession session) throws JsonProcessingException {
        String username = (String) session.getAttribute("username");
        String userid = (String) session.getAttribute("userid");

        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not logged in");
        }

        Account account = db.getAccount(username);

        if (account == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid account");
        }

        ObjectNode response = om.createObjectNode();

        ArrayList<ObjectId> p = account.getPosts();

        Iterator<ObjectId> posts = p.iterator();
        int postCount = 0;
        while (posts.hasNext()) {
            Post currentPost = db.getPost(posts.next());
            if (currentPost == null) continue;
            ObjectNode currentArrayNode = response.putObject(String.valueOf(postCount));
            currentArrayNode.put("postid", currentPost.get_id().toHexString());
            currentArrayNode.putPOJO("images", currentPost.getImageId());
            currentArrayNode.put("accountid", currentPost.getAccount().toHexString());
            currentArrayNode.put("description", currentPost.getDescription());
            currentArrayNode.put("likes", currentPost.getLikes());
            currentArrayNode.putPOJO("tags", currentPost.getTags());
            currentArrayNode.put("date", currentPost.getDate().toString());
            currentArrayNode.putPOJO("comments", currentPost.getComments());
            postCount++;
        }
        return om.writeValueAsString(response);
    }

    @GetMapping(value = "/feed", produces = "application/json")
    public @ResponseBody ArrayList<Post> feed(HttpSession session) {
        String username = (String) session.getAttribute("username");
        String userid = (String) session.getAttribute("userid");
        if (username == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not logged in");
        Account account = db.getAccount(username);
        if (account == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid account");

        ObjectNode response = om.createObjectNode();

        ArrayList<ObjectId> followedUsers = account.getFollowedUsers();
        Iterator<ObjectId> followedUsersItr = followedUsers.iterator();
        ArrayList<Post> allPosts = new ArrayList<>();
        while(followedUsersItr.hasNext()) {
            ObjectId currentUserId = followedUsersItr.next();
            Account currentUser = db.getAccount(currentUserId);
            ArrayList<ObjectId> posts = currentUser.getPosts();
            Iterator<ObjectId> postItr = posts.iterator();
            while(postItr.hasNext()){
                ObjectId currentPost = postItr.next();
                Post p = db.getPost(currentPost);
                if(p == null) continue;
                allPosts.add(db.getPost(currentPost));
            }
        }
        return allPosts;
    }

    @GetMapping(value = "/accountimages")
    public @ResponseBody ArrayList<Image> getAccountImages(HttpSession session) throws JsonProcessingException {
        String username = (String) session.getAttribute("username");
        String userid = (String) session.getAttribute("userid");
        if (username == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not logged in");
        Account account = db.getAccount(username);
        if (account == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid account");
        ArrayList<Image> response = new ArrayList<>();
        ArrayList<ObjectId> p = account.getPosts();
        Iterator<ObjectId> itr = p.iterator();
        while(itr.hasNext()) {
            ObjectId currentPostId = itr.next();
            Post currentPost = db.getPost(currentPostId);
            Iterator<String> itr2 = currentPost.getImageId().iterator();
            while(itr2.hasNext()) {
                Image i = db.getImage(itr2.next());
                response.add(i);
            }
        }
        return response;
    }
}