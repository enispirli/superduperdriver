package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("select * from CREDENTIALS where userid = #{userId}")
    List<Credential> getCredentialList(Integer userId);

    @Select("select * from CREDENTIALS where credentialid = #{credentialId}")
    Credential getCredential(Integer credentialId);

    @Insert("insert into CREDENTIALS (url, username, key, password, userid) " +
            "values(#{url}, #{userName}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    void insertCredential(Credential credential);

    @Update("update CREDENTIALS set url = #{url}, key = #{key}, password = #{password}, username = #{newUserName} where credentialid = #{credentialId}")
    void updateCredential(Integer credentialId, String newUserName, String url, String key, String password);

    @Delete("delete from CREDENTIALS where credentialid = #{credentialId}")
    void deleteCredential(Integer credentialId);

}
