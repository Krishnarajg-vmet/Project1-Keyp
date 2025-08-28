package com.kay.keyp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.kay.keyp.dto.UsersDto;
import com.kay.keyp.entity.Users;
import com.kay.keyp.service.CredentialService;
import com.kay.keyp.service.UsersService;

@Controller
public class ViewController {
	
	@Autowired
    private CredentialService credentialService;
    
    @Autowired
    private UsersService usersService;

	@GetMapping("/login")
	public String loginPage() {
	    return "login";
	}
	
	@GetMapping("/home")
    public String listCredentials(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Users currentUser = usersService.findByUsername(userDetails.getUsername()).orElseThrow();
        model.addAttribute("credentials", credentialService.getAllCredentialByUsers(currentUser));
        return "home";
    }
	
	@GetMapping("/public/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UsersDto());
        return "user-register";
    }
	
	@GetMapping("/reset-password")
    public String showResetPasswordForm() {
        return "reset-password";
    }
	
}
