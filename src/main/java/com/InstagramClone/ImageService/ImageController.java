package com.InstagramClone.ImageService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.InstagramClone.model.Account;
import com.InstagramClone.model.Post;
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

import javax.servlet.http.HttpSession;

@RestController
public class ImageController {
    private final DatabaseController db = DatabaseController.getInstance();
    private ObjectMapper om = new ObjectMapper();

    @GetMapping("/image/{id:.+}")
    public @ResponseBody ResponseEntity<byte[]> getImage(@PathVariable String id) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        Image i = db.getImage(id);

        return new ResponseEntity<>(i.getImageFile(), headers, HttpStatus.CREATED);
    }

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
        } try (InputStream inputStream = file.getInputStream()) {
            byte[] imageFile = inputStream.readAllBytes();
            Image i = new Image(imageFile);
            db.insertImage(i);
            ObjectNode response = om.createObjectNode();
            response.put("_id", i.get_id());
            return om.writeValueAsString(response);
        } catch (IOException e) {
            throw new IOException("Failed to store file ", e);
        }
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
        } try (InputStream inputStream = images.getInputStream()) {
            Account a = db.getAccount(username);
            byte[] imageFile = inputStream.readAllBytes();
            Image i = new Image(imageFile);

            db.insertImage(i);
            ArrayList<String> imageList = new ArrayList<>();
            imageList.add(i.get_id());
            Post p = new Post(imageList, a._id, description);
            db.insertPost(p);
            ObjectNode response = om.createObjectNode();
            response.put("status", "success");
            response.put("_id", i.get_id());
            return om.writeValueAsString(response);
        } catch (IOException e) {
            throw new IOException("Failed to store file ", e);
        }
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