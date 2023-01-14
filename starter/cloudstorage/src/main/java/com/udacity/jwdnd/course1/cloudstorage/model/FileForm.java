package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileForm {
    private MultipartFile file;

}
