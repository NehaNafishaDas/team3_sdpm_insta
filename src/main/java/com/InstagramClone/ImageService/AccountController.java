package com.InstagramClone.ImageService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.InstagramClone.models.Account;

@RestController
public class AccountController {
    @PostMapping("/signup")
    public String signUp(@RequestParam String username, @RequestParam String password) {
    	DatabaseController db = DatabaseController.getInstance();
    	Account account = new Account(username, password);
    	return db.createAccount(account).toString();
    }
}
