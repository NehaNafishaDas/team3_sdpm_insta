package com.InstagramClone.ImageService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.InstagramClone.model.Post;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.InstagramClone.model.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AccountController {
    private ObjectMapper om = new ObjectMapper();
    private final DatabaseController db = DatabaseController.getInstance();
    private Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dongodxek",
            "api_key", "473417739651645",
            "api_secret", "C6P529y3ejZcBSeVyqh-4Opeo1w"));

    // Creates account given a username and password
    @PostMapping(value = "/signup", produces = "application/json")
    public String signUp(@RequestParam String username, @RequestParam String password) throws JsonProcessingException, NoSuchAlgorithmException {
        ObjectNode response = om.createObjectNode();

        if(username.equals("")){
            response.put("username", "Username is required");
        }
        if(password.equals("")){
            response.put("password", "Password is required");
        }
        if(response.size()>0) return om.writeValueAsString(response);

        if(username == null || password == null){
            response.put("status", "failed");
            response.put("error", "Please enter both a username and password");
            return om.writeValueAsString(response);
        }
        Account account = new Account(username, password);
        if (db.getAccount(username) != null) {
            response.put("status", "failed");
            response.put("error", "Account already exists with username " + username);
            return om.writeValueAsString(response);
        } else {
            String id = db.createAccount(account);
            response.put("status", "success");
            response.put("username", username);
            response.put("_id", id);
            return om.writeValueAsString(response);
        }
    }

    @PostMapping(value = "/signin", produces = "application/json")
    public String signIn(@RequestParam String username, @RequestParam String password, HttpSession session) throws JsonProcessingException, NoSuchAlgorithmException {
        if(username == null || password == null){
            ObjectNode response = om.createObjectNode();
            response.put("status", "failed");
            response.put("error", "Please enter both a username and password");
            return om.writeValueAsString(response);
        }
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

    @PostMapping(value = "/changepassword", produces = "application/json")
    public String changePassword(@RequestParam String oldpassword,
                                 @RequestParam String newpassword,
                                 HttpSession session) throws JsonProcessingException {
        String username = (String) session.getAttribute("username");
        String userid = (String) session.getAttribute("userid");
        ObjectNode response = om.createObjectNode();
        if (username == null) {
            response.put("status", "failed");
            response.put("error", "Not logged in");
            return om.writeValueAsString(response);
        } else {
            Account a = db.getAccount(username);
            if(a == null)
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid account");

            if(a.getPassword().equals(oldpassword)) {
                db.changePassword(a._id, newpassword);
                response.put("status", "success");
                return om.writeValueAsString(response);
            } else {
                response.put("status", "failed");
                response.put("error", "Old password incorrect");
                return om.writeValueAsString(response);
            }
        }
    }

    @PostMapping(value = "/changeemail", produces = "application/json")
    public String changeEmail(@RequestParam String newemail,
                                 HttpSession session) throws JsonProcessingException {
        String username = (String) session.getAttribute("username");
        String userid = (String) session.getAttribute("userid");
        ObjectNode response = om.createObjectNode();
        if (username == null) {
            response.put("status", "failed");
            response.put("error", "Not logged in");
            return om.writeValueAsString(response);
        } else {
            Account a = db.getAccount(username);
            if(a == null)
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid account");
            db.changeEmail(a._id, newemail);
            response.put("status", "success");
            return om.writeValueAsString(response);
        }
    }

    @PostMapping(value = "/changebio", produces = "application/json")
    public String changeBio(@RequestParam String bio,
                              HttpSession session) throws JsonProcessingException {
        String username = (String) session.getAttribute("username");
        String userid = (String) session.getAttribute("userid");
        ObjectNode response = om.createObjectNode();
        if (username == null) {
            response.put("status", "failed");
            response.put("error", "Not logged in");
            return om.writeValueAsString(response);
        } else {
            Account a = db.getAccount(username);
            if(a == null)
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid account");
            db.changeBio(a._id, bio);
            response.put("status", "success");
            return om.writeValueAsString(response);
        }
    }

    @PutMapping(value = "/updateprofile", produces = "application/json")
    public String updateProfile(@RequestParam(required = false) String firstname,
                                @RequestParam(required = false) String lastname,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String bio,
                                @RequestBody(required = false) MultipartFile image,
                                HttpSession session) throws IOException {
        String username = (String) session.getAttribute("username");
        String userid = (String) session.getAttribute("userid");
        ObjectNode response = om.createObjectNode();
        if (username == null) {
            response.put("status", "failed");
            response.put("error", "Not logged in");
            return om.writeValueAsString(response);
        } else {
            Account a = db.getAccount(username);
            if(a == null) {
                response.put("status", "failed");
                response.put("error", "could not find accounts");
                return om.writeValueAsString(response);
            }

            if (firstname != null) {
                db.changeFirstname(a._id, firstname);
                response.put("firstname", "updated");
            }
            if (lastname != null) {
                db.changeLastname(a._id, lastname);
                response.put("lastname", "updated");
            }
            if (email != null) {
                db.changeEmail(a._id, email);
                response.put("email", "updated");
            }
            if (bio != null) {
                db.changeBio(a._id, bio);
                response.put("bio", "updated");
            }
            if(image != null) {
                Map uploadResult = cloudinary.uploader()
                        .upload(image.getBytes(), ObjectUtils.emptyMap());
                String url = (String) uploadResult.get("url");
                db.setProfilePicture(a._id, url);
                response.put("image", "updated");
            }
            return om.writeValueAsString(response);
        }
    }

    @PostMapping(value = "/followtoggle", produces = "application/json")
    public String followToggle(@RequestParam String targetaccount, HttpSession session) throws IOException {
        String username = (String) session.getAttribute("username");
        String userid = (String) session.getAttribute("userid");
        ObjectNode response = om.createObjectNode();
        if (username == null) {
            response.put("status", "failed");
            response.put("error", "Not logged in");
            return om.writeValueAsString(response);
        } else {
            Account a = db.getAccount(username);
            Account target = db.getAccount(targetaccount);
            if(a == null || target == null) {
                response.put("status", "failed");
                response.put("error", "could not find accounts");
                return om.writeValueAsString(response);
            }
            if(!a.followedUsers.contains(target._id)) {
                db.followUser(target._id, a._id);
                response.put("status", "success");
                response.put("error", "Now following user " + targetaccount);
                return om.writeValueAsString(response);
            } else {
                db.unfollowUser(target._id, a._id);
                response.put("status", "success");
                response.put("error", "Unfollowed user " + targetaccount);
                return om.writeValueAsString(response);
            }
        }
    }

    @GetMapping(value = "/isfollowing", produces = "application/json")
    public String isFollowing(@RequestParam String targetaccount, HttpSession session ) throws JsonProcessingException {
        String username = (String) session.getAttribute("username");
        String userid = (String) session.getAttribute("userid");
        ObjectNode response = om.createObjectNode();
        if (username == null) {
            response.put("status", "failed");
            response.put("error", "Not logged in");
            return om.writeValueAsString(response);
        } else {
            Account user = db.getAccount(username);
            Account target = db.getAccount(targetaccount);
            if(user == null || target == null) {
                response.put("status", "failed");
                response.put("error", "User not found");
                return om.writeValueAsString(response);
            }
            if (user.followedUsers.contains(target._id)) {
                response.put("isfollowing", true);
                response.put("account", targetaccount);
                return om.writeValueAsString(response);
            } else {
                response.put("isfollowing", false);
                response.put("account", targetaccount);
                return om.writeValueAsString(response);
            }
        }
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

    @GetMapping(value = "/getuser", produces = "application/json")
    public @ResponseBody
    String getUser(@RequestParam String userid) throws IOException {
        if(userid == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no userid received");

        Account account = db.getAccount(userid);
        ObjectNode response = om.createObjectNode();
        response.put("_id", account.get_id());
        response.put("username", account.getUsername());
        response.put("firstName", account.getFirstName());
        response.put("lastName", account.getLastName());
        response.put("bio", account.getBio());
        response.put("email", account.getEmail());
        response.put("profilepicture", account.getProfilepicture());
        response.put("followercount", account.getFollowedBy().size());
        response.put("followingcount", account.getFollowedUsers().size());
        ArrayList<ObjectId> postids = account.getPosts();
        Iterator<ObjectId> iterator = postids.iterator();
        ArrayList<Post> posts = new ArrayList<>();
        while(iterator.hasNext()) {
            Post p = db.getPost(iterator.next());
            posts.add(p);
        }
        response.putPOJO("posts", posts);
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
        Post p = new Post(imageList, a._id, a.getUsername(), description);
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
        if (username == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not logged in");
        Account account = db.getAccount(username);
        if (account == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid account");

        ObjectNode response = om.createObjectNode();
        response.put("_id", account.get_id());
        response.put("username", account.getUsername());
        response.put("firstName", account.getFirstName());
        response.put("lastName", account.getLastName());
        response.put("bio", account.getBio());
        response.put("email", account.getEmail());
        response.put("profilepicture", account.getProfilepicture());
        response.put("followercount", account.getFollowedBy().size());
        response.put("followingcount", account.getFollowedUsers().size());

        ArrayList<Post> allPosts = new ArrayList<>();
        ArrayList<ObjectId> myPosts = account.getPosts();
        Iterator<ObjectId> myPostItr = myPosts.iterator();
        while(myPostItr.hasNext()){
            ObjectId currentPost = myPostItr.next();
            Post p = db.getPost(currentPost);
            if(p == null) continue;
            allPosts.add(db.getPost(currentPost));
        }
        Collections.sort(allPosts, Collections.reverseOrder());
        response.putPOJO("posts", allPosts);
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

        ArrayList<ObjectId> followedUsers = account.getFollowedUsers();
        Iterator<ObjectId> followedUsersItr = followedUsers.iterator();
        ArrayList<Post> allPosts = new ArrayList<>();

        ArrayList<ObjectId> myPosts = account.getPosts();
        Iterator<ObjectId> myPostItr = myPosts.iterator();
        while(myPostItr.hasNext()){
            ObjectId currentPost = myPostItr.next();
            Post p = db.getPost(currentPost);
            if(p == null) continue;
            allPosts.add(db.getPost(currentPost));
        }

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
        Collections.sort(allPosts, Collections.reverseOrder());
        return allPosts;
    }

    // returns the 9 highest liked public posts
    @GetMapping(value = "/popularposts", produces = "application/json")
    public @ResponseBody ArrayList<Post> popularPosts() {
        return db.getPopularPosts();
    }
}