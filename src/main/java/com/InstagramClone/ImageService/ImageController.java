package com.InstagramClone.ImageService;

 
import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageController {
    private final ImageStorageService imageStorageService = new ImageStorageService();
    
    @GetMapping("/image/{id:.+}")
    public @ResponseBody ResponseEntity<byte[]> getImage(@PathVariable String id) throws IOException {
        String imageName = id;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        DatabaseController db = DatabaseController.getInstance();
        Image i = db.getImage(imageName);
        
        return new ResponseEntity<byte[]>(i.getImageFile(), headers, HttpStatus.CREATED);
    }
    
    @PostMapping("/imageUpload")
    public String postImage(@RequestParam("file") MultipartFile file, @RequestParam(required = false) String account, @RequestParam(required = false) String description) {
        try {
        	Image imageID = imageStorageService.post(file, account, description);
	        return imageID.getId().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return "error";
    }
    
    @GetMapping("/account/{account:.+}")
    public @ResponseBody String getAccountImages(@PathVariable String account) throws IOException {
        return imageStorageService.getPostsFromAccount(account);
    }

}