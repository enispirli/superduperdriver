package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("note")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    private final UserService userService;

    @GetMapping
    public String getNotTab(
            Authentication authentication,
            @ModelAttribute("newNote") NoteForm newNote,
            Model model) {
        Integer userId = getUserId(authentication);
        model.addAttribute("notes", this.noteService.getNoteList(userId));

        return "home";
    }

    @GetMapping(value = "{noteId}")
    public Note getNote(@PathVariable Integer noteId) {
        return noteService.getNote(noteId);
    }


    @PostMapping
    public String newNote(
            Authentication authentication,
            @ModelAttribute("newNote") NoteForm newNote,
            Model model) {

        String noteTitle = newNote.getTitle();
        Integer noteId = newNote.getNoteId();
        String newDescription = newNote.getDescription();
        if (noteId == null){
            noteService.addNote(noteTitle, newDescription, getUserId(authentication));
        } else{
            Note existingNote = getNote(noteId);
            noteService.updateNote(existingNote.getNoteId(), noteTitle, newDescription);
        }
        Integer userId = getUserId(authentication);
        model.addAttribute("notes", noteService.getNoteList(userId));
        model.addAttribute("result", "success");

        return "result";
    }

    @GetMapping(value = "/delete/{noteId}")
    public String deleteNote(
            Authentication authentication,
            @PathVariable Integer noteId,
            @ModelAttribute("newNote") NoteForm newNote,
            Model model) {
        Integer userId = getUserId(authentication);
        noteService.deleteNote(noteId);
        model.addAttribute("notes", noteService.getNoteList(userId));
        model.addAttribute("result", "success");

        return "result";
    }

    private Integer getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        return user.getUserId();
    }
}
