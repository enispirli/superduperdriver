package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;


@Controller
@RequestMapping("credential")
@RequiredArgsConstructor
public class CredentialController {

    private final CredentialService credentialService;

    private final EncryptionService encryptionService;

    private final UserService userService;

    @GetMapping
    public String getCredentialTab(
            Authentication authentication,
            @ModelAttribute("newCredential") CredentialForm newCredential,
            Model model) {
        Integer userId = getUserId(authentication);
        model.addAttribute("credentials", this.credentialService.getCredentialList(userId));
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }

    @GetMapping(value = "/{credentialId}")
    public Credential getCredential(@PathVariable Integer credentialId) {
        return credentialService.getCredential(credentialId);
    }

    @PostMapping
    public String newCredential(
            Authentication authentication,
            @ModelAttribute("newCredential") CredentialForm newCredential,
            Model model) {
        Integer userId = getUserId(authentication);
        String newUrl = newCredential.getUrl();
        Integer credentialId = newCredential.getCredentialId();
        String password = newCredential.getPassword();

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);

        if (credentialId == null) {
            credentialService.addCredential(newUrl, userId, newCredential.getUserName(), encodedKey, encryptedPassword);
        } else {
            Credential existingCredential = getCredential(credentialId);
            credentialService.updateCredential(existingCredential.getCredentialId(), newCredential.getUserName(), newUrl, encodedKey, encryptedPassword);
        }
        model.addAttribute("credentials", credentialService.getCredentialList(userId));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("result", "success");

        return "result";
    }

    @GetMapping(value = "/delete/{credentialId}")
    public String deleteCredential(
            Authentication authentication,
            @PathVariable Integer credentialId,
            Model model) {
        Integer userId = getUserId(authentication);
        credentialService.deleteCredential(credentialId);
        model.addAttribute("credentials", credentialService.getCredentialList(userId));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("result", "success");

        return "result";
    }

    private Integer getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        return user.getUserId();
    }
}
