package com.InstagramClone.ImageService;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.InstagramClone.models.Account;

@RestController
public class AccountController {
    private final ImageStorageService imageStorageService = new ImageStorageService();
	
    @PostMapping("/signup")
    public Account signUp(@RequestParam String username, @RequestParam String password) {
    	DatabaseController db = DatabaseController.getInstance();
    	Account account = new Account(username, password);
    	db.createAccount(account);
    	return account;
    }
    
    @GetMapping("/account/{account:.+}")
    public @ResponseBody String getAccountImages(@PathVariable String account) throws IOException {
        return imageStorageService.getPostsFromAccount(account);
    }

}
