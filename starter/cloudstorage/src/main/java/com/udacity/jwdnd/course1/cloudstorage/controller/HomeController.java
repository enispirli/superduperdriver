package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;

    private final FileService fileService;

    private final NoteService noteService;

    @GetMapping
    public String getHomePage(Authentication authentication,
                              @ModelAttribute("newFile") FileForm newFile,
                              @ModelAttribute("newNote") NoteForm newNote,
                              Model model) {
        Integer userId = getUserId(authentication);
        model.addAttribute("files", fileService.getFileList(userId));
        model.addAttribute("notes", noteService.getNoteList(userId));
        return "home";
    }

    @GetMapping(
            value = "/file/{fileName}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public @ResponseBody
    byte[] getFile(@PathVariable String fileName) {
        return fileService.getFile(fileName).getFileData();
    }

    @PostMapping
    public String newFile(
            Authentication authentication,
            @ModelAttribute("newFile") FileForm newFile,
            Model model) throws IOException {

        MultipartFile multipartFile = newFile.getFile();
        if (StringUtils.isEmpty(multipartFile.getName()) || multipartFile.isEmpty()) {
            model.addAttribute("result", "error");
            model.addAttribute("message", "File is empty");
            return "result";
        }

        Integer userId = getUserId(authentication);

        boolean fileIsExist = isFileExist(userId, multipartFile.getOriginalFilename());

        if (!fileIsExist) {
            fileService.addFile(multipartFile, userId);
            model.addAttribute("result", "success");
        } else {
            model.addAttribute("result", "error");
            model.addAttribute("message", "You have tried to add a duplicate file.");
        }
        model.addAttribute("files", fileService.getFileList(userId));

        return "result";
    }

    @GetMapping(value = "/file/delete/{fileName}")
    public String deleteFile(
            Authentication authentication,
            @PathVariable String fileName,
            @ModelAttribute("newFile") FileForm newFile,
            Model model) {
        fileService.deleteFile(fileName);
        Integer userId = getUserId(authentication);
        model.addAttribute("files", fileService.getFileList(userId));
        model.addAttribute("result", "success");

        return "result";
    }

    private boolean isFileExist(Integer userId,
                                String fileName) {
        return fileService.getFileList(userId).stream()
                .anyMatch(file -> file.equals(fileName));

    }

    private Integer getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        return user.getUserId();
    }
}
