package com.InstagramClone.ImageService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempWebController {
	
    @GetMapping("/")
    public String greeting() {
        return "UploadForm";
    }

}
