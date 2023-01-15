package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credential {

    private Integer credentialId;

    private String url;

    private String userName;

    private String key;

    private String password;

    private Integer userId;
}
