package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CredentialService {

    private final CredentialMapper credentialMapper;

    public List<Credential> getCredentialList(Integer userId) {
        return credentialMapper.getCredentialList(userId);
    }

    public Credential getCredential(Integer credentialId) {
        return credentialMapper.getCredential(credentialId);
    }

    public void addCredential(String url, Integer userId , String credentialUserName, String key, String password) {
        Credential credential = new Credential(0, url, credentialUserName, key, password, userId);
        credentialMapper.insertCredential(credential);
    }

    public void updateCredential(Integer credentialId, String newUserName, String url, String key, String password) {
        credentialMapper.updateCredential(credentialId, newUserName, url, key, password);
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.deleteCredential(credentialId);
    }
}
