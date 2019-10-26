package com.InstagramClone.ImageService;

import org.bson.types.ObjectId;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.InstagramClone.models.Account;
import com.InstagramClone.models.Image;
import com.InstagramClone.models.Post;
import com.mongodb.client.FindIterable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import static com.mongodb.client.model.Filters.eq;

public class ImageStorageService {
	
    private DatabaseController db = DatabaseController.getInstance();

	public Image post(MultipartFile file, String account, String description) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new IOException("Failed to store empty file " + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
            	byte[] imageFile = inputStream.readAllBytes();
            	Image i = new Image(imageFile);
            	db.insertImage(i);
            	ArrayList<String> images = new ArrayList<>();
            	images.add(i.get_id());
            	Post post = new Post(images, account, description);
            	db.makePost(post);
                return i;
            }
        }
        
        catch (IOException e) {
            throw new IOException("Failed to store file " + filename, e);
        }
    }
    
    public String getPostsFromAccount(String account) {
    	FindIterable<Post> r = db.postFind(eq("account", account));
    	return r.toString();
    }
    
    public Account getAccount(ObjectId requestId) {
    	return db.getAccount(requestId);
    }

    public String load(String id) {
    	DatabaseController db = DatabaseController.getInstance();
    	db.getImage(id);
    	return "";
    }
}