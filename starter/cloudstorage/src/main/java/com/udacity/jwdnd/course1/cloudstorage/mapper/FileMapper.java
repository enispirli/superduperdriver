package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("select * from FILES where filename = #{fileName}")
    File getFile(String fileName);

    @Select("select filename from FILES where userid = #{userId}")
    List<String> getFileList(Integer userId);

    @Insert("insert into FILES (filename, contenttype, filesize, userid, filedata) " +
            "values(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Delete("delete from FILES where filename = #{fileName}")
    void deleteFile(String fileName);
}
