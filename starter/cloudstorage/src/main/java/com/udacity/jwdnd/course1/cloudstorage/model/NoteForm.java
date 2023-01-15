package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.Data;

@Data
public class NoteForm {

    private Integer noteId;

    private String title;

    private String description;
}
