package com.kay.keyp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.kay.keyp.dto.CredentialDto;
import com.kay.keyp.entity.Users;
import com.kay.keyp.enums.AccountType;
import com.kay.keyp.service.CredentialService;
import com.kay.keyp.service.UsersService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/credentials")
public class CredentialController {

    @Autowired
    private CredentialService credentialService;
    
    @Autowired
    private UsersService usersService;

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("credential", new CredentialDto());
        model.addAttribute("accountTypes", AccountType.values());
        return "credential-form";
    }

    @PostMapping("/save")
    public String saveCredential(@Valid @ModelAttribute("credential") CredentialDto credential,
                                 BindingResult result, Model model,
                                 @AuthenticationPrincipal UserDetails userDetails) {

        if (result.hasErrors()) {
            model.addAttribute("accountTypes", AccountType.values());
            return "credential-form";
        }

        Users currentUser = usersService.findByUsername(userDetails.getUsername())
                             .orElseThrow(() -> new RuntimeException("User not found"));

        credentialService.saveCredentials(credential, currentUser);

        return "redirect:/home";
    }


    @GetMapping("/edit/{id}")
    public String editCredential(@PathVariable Long id, Model model) {
        CredentialDto dto = credentialService.getByCredentialId(id);
        if (dto == null) {
            return "redirect:/credentials";
        }
        model.addAttribute("credential", dto);
        model.addAttribute("accountTypes", AccountType.values());
        return "credential-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteCredential(@PathVariable Long id) {
        credentialService.deactivateCredentials(id);
        return "redirect:/credentials";
    }
}