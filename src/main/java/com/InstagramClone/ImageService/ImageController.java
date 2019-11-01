package com.InstagramClone.ImageService;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import com.InstagramClone.model.Account;
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
        Post p = new Post(imageList, a._id, description);
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
            a = db.getAccount(new ObjectId(loggedInAs));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid object id");
        }
        if(p == null || a == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid request");
        } else {
            db.likePost(a, p);
            ObjectNode response = om.createObjectNode();
            response.put("_id", p.get_id());
            response.put("like", p.getLikes()+1);
            return om.writeValueAsString(response);
        }
    }
}