package com.insta;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
	
	@RequestMapping("/") 
    public String showLogin() {
        return "login";
    }
	
	@RequestMapping("/signUp") 
    public String signUpDetails() {
        return "signUp";
    }

}
