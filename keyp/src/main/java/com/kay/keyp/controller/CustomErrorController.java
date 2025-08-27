package com.kay.keyp.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    @GetMapping
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object uri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        String error;
        if (message == null || ((String) message).isEmpty()) {
            Integer statusCode = status != null ? Integer.valueOf(status.toString()) : 0;
            error = (statusCode != 0) ? HttpStatus.valueOf(statusCode).getReasonPhrase() : "Error";
        } else {
            error = message.toString();
        }

        model.addAttribute("status", status != null ? status : "Unknown");
        model.addAttribute("error", error);
        model.addAttribute("message", message != null ? message : "No details available");
        model.addAttribute("path", uri != null ? uri : "N/A");
        model.addAttribute("timestamp", java.time.LocalDateTime.now());

        return "error";
    }

    public String getErrorPath() {
        return "/error";
    }
    
    @GetMapping("/sessionInvalid")
    public String sessionInvalid(Model model) {
        model.addAttribute("status", "Session Invalid");
        model.addAttribute("error", "Your session is invalid.");
        model.addAttribute("message", "Please log in again.");
        model.addAttribute("path", "/sessionInvalid");
        model.addAttribute("timestamp", java.time.LocalDateTime.now());
        return "error"; // your error.html
    }

    @GetMapping("/sessionExpired")
    public String sessionExpired(Model model) {
        model.addAttribute("status", "Session Expired");
        model.addAttribute("error", "Your session has expired.");
        model.addAttribute("message", "Please log in again to continue.");
        model.addAttribute("path", "/sessionExpired");
        model.addAttribute("timestamp", java.time.LocalDateTime.now());
        return "error"; // your error.html
    }
}
