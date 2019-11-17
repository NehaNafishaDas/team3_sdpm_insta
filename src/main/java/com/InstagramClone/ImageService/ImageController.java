package com.InstagramClone.ImageService;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import com.InstagramClone.model.Account;
import com.InstagramClone.model.Comment;
import com.InstagramClone.model.Post;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.types.ObjectId;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.InstagramClone.model.Image;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

@RestController
public class ImageController {
    private final DatabaseController db = DatabaseController.getInstance();
    private ObjectMapper om = new ObjectMapper();
    private Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dongodxek",
            "api_key", "473417739651645",
            "api_secret", "C6P529y3ejZcBSeVyqh-4Opeo1w"));


//    @GetMapping("/image/{id:.+}")
//    public @ResponseBody ResponseEntity<byte[]> getImage(@PathVariable String id) {
//        final HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG);
//        ObjectId imageid;
//        try {
//            imageid = new ObjectId(id);
//        } catch (IllegalArgumentException e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid id");
//        }
//        Image i = db.getImage(imageid.toHexString());
//        if(i == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "image not found");
//        }
//        return new ResponseEntity<>(i.getImageFile(), headers, HttpStatus.CREATED);
//    }

    @PostMapping(value = "/imageupload", produces = "application/json")
    public @ResponseBody String uploadImage(@RequestBody MultipartFile file,
                                            @RequestParam(required = false) String description,
                                            HttpSession session) throws IOException {
        String loggedInAs = (String) session.getAttribute("username");
        if(loggedInAs == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not logged in");
        }
        if(file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "could not accept file");
        }
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

        return uploadResult.toString();
    }

    @PostMapping(value = "/imagepost", produces = "application/json")
    public @ResponseBody String imagePost(@RequestParam("images") MultipartFile images,
                                            @RequestParam(required = false) String description,
                                            HttpSession session) throws IOException {
        String username = (String) session.getAttribute("username");
        if(username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not logged in");
        }
        if(images.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "could not accept file");
        }
        Account a = db.getAccount(username);
        Map uploadResult = cloudinary.uploader().upload(images.getBytes(), ObjectUtils.emptyMap());
        ArrayList<String> imageList = new ArrayList<>();
        imageList.add((String)uploadResult.get("url"));
        Post p = new Post(imageList, a._id, a.getUsername(), description);
        db.insertPost(p);
        ObjectNode response = om.createObjectNode();
        response.put("status", "success");
        response.put("url", (String)uploadResult.get("url"));
        return om.writeValueAsString(response);
    }

    @PostMapping(value = "/likepost", produces = "application/json")
    public @ResponseBody String likePost(@RequestParam String postid,
                                         HttpSession session) throws JsonProcessingException {
        String loggedInAs = (String) session.getAttribute("username");
        if(loggedInAs == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not logged in");
        }
        Post p;
        Account a;
        try {
            p = db.getPost(new ObjectId(postid));
            a = db.getAccount(loggedInAs);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid object id");
        }
        if(p == null || a == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid request");
        } else {
            if(db.likePost(a, p)) {
                ObjectNode response = om.createObjectNode();
                response.put("_id", p.get_id().toHexString());
                response.put("likes", p.getLikes() + 1);
                return om.writeValueAsString(response);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "already liked");
            }
        }
    }

    @PostMapping(value = "/unlikepost", produces = "application/json")
    public @ResponseBody String unlikePost(@RequestParam String postid,
                                         HttpSession session) throws JsonProcessingException {
        String loggedInAs = (String) session.getAttribute("username");
        if(loggedInAs == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not logged in");
        }
        Post p;
        Account a;
        try {
            p = db.getPost(new ObjectId(postid));
            a = db.getAccount(loggedInAs);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid object id");
        }
        if(p == null || a == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid request");
        } else {
            if (db.unlikePost(a, p)) {
                ObjectNode response = om.createObjectNode();
                response.put("_id", p.get_id().toHexString());
                response.put("likes", p.getLikes() - 1);
                return om.writeValueAsString(response);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "already unliked");
            }
        }
    }

    @PostMapping(value = "/liketoggle", produces = "application/json")
    public @ResponseBody String likeTogglePost(@RequestParam String postid,
                                         HttpSession session) throws JsonProcessingException {
        String loggedInAs = (String) session.getAttribute("username");
        if(loggedInAs == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not logged in");
        }
        Post p;
        Account a;
        try {
            p = db.getPost(new ObjectId(postid));
            a = db.getAccount(loggedInAs);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid object id");
        }
        if(p == null || a == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid request");
        } else {
            ArrayList<ObjectId> likedPosts = a.getLikedPosts();
            if(!likedPosts.contains(p._id)) {
                db.likePost(a, p);
                ObjectNode response = om.createObjectNode();
                response.put("_id", p.get_id().toHexString());
                response.put("liked", "true");
                response.put("likes", p.getLikes() + 1);
                return om.writeValueAsString(response);
            } else {
                db.unlikePost(a, p);
                ObjectNode response = om.createObjectNode();
                response.put("_id", p.get_id().toHexString());
                response.put("liked", "false");
                response.put("likes", p.getLikes() - 1);
                return om.writeValueAsString(response);
            }
        }
    }

    @GetMapping(value = "/isliked", produces = "application/json")
    public @ResponseBody String isLiked(@RequestParam String postid, @RequestParam String username) throws JsonProcessingException {
        Account a;
        ObjectId p;
        try {
            a = db.getAccount(username);
            p = new ObjectId(postid);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid object id");
        }
        if (a == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid request");

        ArrayList<ObjectId> likedPosts = a.getLikedPosts();
        ObjectNode response = om.createObjectNode();
        response.put("_id", p.toHexString());

        if (likedPosts.contains(p))
            response.put("liked", "true");
        else
            response.put("liked", "false");

        return om.writeValueAsString(response);
    }

    @PostMapping(value = "/writecomment", produces = "application/json")
    public @ResponseBody String writeComment(@RequestParam String postid,
                                             @RequestParam String comment,
                                             @RequestBody(required = false) MultipartFile image,
                                             HttpSession session) throws IOException {
        String username = (String) session.getAttribute("username");
        if(username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not logged in");
        }
        Post p;
        Account a;
        try {
            p = db.getPost(new ObjectId(postid));
            a = db.getAccount(username);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid post or account");
        }

        if(p == null || a == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid request");
        } else {
            if(image != null) {
                Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = (String) uploadResult.get("url");
                db.writeComment(a, p, comment, imageUrl);
            } else {
                db.writeComment(a, p, comment);
            }
            ObjectMapper om = new ObjectMapper();
            ObjectNode response = om.createObjectNode();
            response.put("status", "success");
            return om.writeValueAsString(response);
        }
    }

    @GetMapping(value = "/getcommentsfrompost", produces = "application/json")
    public @ResponseBody ArrayList<Comment> getCommentsFromPost(@RequestParam String postid) {
        Post p;
        try {
            p = db.getPost(new ObjectId(postid));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid post or account");
        }

        if(p == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid request");
        } else {
            return p.getComments();
        }
    }

    @GetMapping(value = "/getpost", produces = "application/json")
    public @ResponseBody String getPost(@RequestParam String postid)
            throws JsonProcessingException {
        Post p;
        try {
            p = db.getPost(new ObjectId(postid));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid post or account");
        }

        if(p == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid request");
        } else {
            ObjectNode response = om.createObjectNode();
            response.put("postid", p.get_id().toHexString());
            response.putPOJO("images", p.getImageId());
            response.put("accountid", p.getAccount().toHexString());
            response.put("description", p.getDescription());
            response.put("likes", p.getLikes());
            response.putPOJO("tags", p.getTags());
            response.put("date", p.getDate().toString());
            return om.writeValueAsString(response);
        }
    }

    @PostMapping(value = "/uploadprofilepicture", produces = "application/json")
    public @ResponseBody String imagePost(@RequestParam("images") MultipartFile images,
                                          HttpSession session) throws IOException {
        String username = (String) session.getAttribute("username");
        if(username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not logged in");
        }
        if(images.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "could not accept file");
        }
        Account a = db.getAccount(username);
        Map uploadResult = cloudinary.uploader().upload(images.getBytes(), ObjectUtils.emptyMap());
        String url = (String) uploadResult.get("url");

        db.setProfilePicture(a._id, url);
        ObjectNode response = om.createObjectNode();
        response.put("status", "success");
        response.put("url", url);
        return om.writeValueAsString(response);
    }
}