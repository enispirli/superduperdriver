package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignupController {

    private final UserService userService;

    @GetMapping()
    public String getSignupPage() {
        return "signup";
    }

    @PostMapping()
    public String signup(@ModelAttribute User user, Model model,
                         RedirectAttributes redirectAttributes) {
        String validError = null;

        if (userService.isUsernameExists(user.getUsername())) {
            validError = "Username already exists.Username: " + user.getUsername();
            model.addAttribute("signupError", validError);
            return "signup";
        }

        int userRow = userService.createUser(user);
        if (userRow < 0) {
            validError = "Something is wrong.Please try again.";
        }

        if (validError == null) {
            redirectAttributes.addFlashAttribute("signupSuccess", true);
            return "redirect:/login";
        } else {
            model.addAttribute("signupError", validError);
        }

        return "signup";
    }
}
