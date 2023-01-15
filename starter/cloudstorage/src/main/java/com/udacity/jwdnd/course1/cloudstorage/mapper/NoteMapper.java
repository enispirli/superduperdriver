package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("select * from NOTES where noteid = #{noteId}")
    Note getNote(Integer noteId);

    @Select("select * from NOTES where userid = #{userId}")
    List<Note> getNoteList(Integer userId);

    @Insert("insert into NOTES (notetitle, notedescription, userid) " +
            "values(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insertNote(Note note);

    @Update("update NOTES set notetitle = #{title}, notedescription = #{description} where noteid = #{noteId}")
    void updateNote(Integer noteId, String title, String description);

    @Delete("delete from NOTES where noteid = #{noteId}")
    void deleteNote(Integer noteId);
}
