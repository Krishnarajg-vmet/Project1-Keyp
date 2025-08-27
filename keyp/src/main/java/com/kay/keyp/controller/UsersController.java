package com.kay.keyp.controller;

import com.kay.keyp.dto.UsersDto;
import com.kay.keyp.entity.Users;
import com.kay.keyp.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UsersDto());
        return "user-register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UsersDto userDto,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "user-register";
        }

        try {
            Users user = usersService.toEntity(userDto);
            usersService.registerUser(user);
        } catch (RuntimeException e) {
            model.addAttribute("registrationError", e.getMessage());
            return "user-register";
        }

        return "redirect:/login";
    }

    @GetMapping("/{id}")
    public String viewUserProfile(@PathVariable Long id, Model model) {
        return usersService.findById(id)
                .map(user -> {
                    UsersDto userDto = usersService.toDto(user);
                    model.addAttribute("user", userDto);
                    return "user-profile";
                })
                .orElse("redirect:/error");
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm() {
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("username") String username,
                                @RequestParam("email") String email,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                Model model) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "reset-password";
        }

        boolean success = usersService.resetPassword(username, email, newPassword);

        if (!success) {
            model.addAttribute("error", "Invalid username or email.");
            return "reset-password";
        }

        model.addAttribute("success", "Password updated successfully.");
        return "reset-password";
    }

    @PostMapping("/update")
    public String updateUserProfile(@ModelAttribute("user") UsersDto userDto, Model model) {
        return usersService.findById(userDto.getId())
                .map(existingUser -> {
                    try {
                        usersService.validateUniqueEmail(userDto.getEmail(), existingUser.getUserId());
                        existingUser.setUsername(userDto.getUsername());
                        existingUser.setEmail(userDto.getEmail());
                        usersService.updateUser(existingUser);
                        return "redirect:/users/" + existingUser.getUserId();
                    } catch (RuntimeException e) {
                        model.addAttribute("error", e.getMessage());
                        model.addAttribute("user", userDto);
                        return "user-profile";
                    }
                })
                .orElse("redirect:/error");
    }
}
