package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteMapper noteMapper;

    public List<Note> getNoteList(Integer userId) {
        return noteMapper.getNoteList(userId);
    }

    public Note getNote(Integer noteId) {
        return noteMapper.getNote(noteId);
    }

    public void addNote(String title, String description, Integer userId) {
        Note note = new Note(0, title, description, userId);
        noteMapper.insertNote(note);
    }

    public void updateNote(Integer noteId, String title, String description) {
        noteMapper.updateNote(noteId, title, description);
    }

    public void deleteNote(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }

}
