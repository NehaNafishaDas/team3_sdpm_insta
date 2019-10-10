package com.InstagramClone.ImageService;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ImageController {
    private final ImageStorageService imageStorageService = new ImageStorageService();
    private final String dir = System.getProperty("user.dir");
    
    @GetMapping("/image/{id:.+}")
    public @ResponseBody ResponseEntity<byte[]> getImage(@PathVariable String id) throws IOException {
        String imageName = imageStorageService.load(id);
    	InputStream in = new BufferedInputStream(new FileInputStream(dir + "/Images/" + imageName));
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
    }
    
    @PostMapping("/")
    public String imageUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

    	String imageID = "";
        try {
			imageID = imageStorageService.store(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
        redirectAttributes.addFlashAttribute("message",
                "Image uploaded: " + file.getOriginalFilename() + " ID: " + imageID);

        return "redirect:/";
    }
    
    @GetMapping("/")
    public String greeting() {
        return "UploadForm";
    }

}