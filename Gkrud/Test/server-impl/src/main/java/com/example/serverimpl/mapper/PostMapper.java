package com.example.serverimpl.mapper;

import com.example.serverimpl.dto.PostDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {
   @Insert("INSERT INTO posts(user_id,title,content) VALUES(#{userId},#{title},#{content})")
   @Options(useGeneratedKeys=true, keyProperty="id")
   void create(PostDTO post);

   @Select("SELECT p.*, u.username FROM posts p JOIN users u ON p.user_id=u.id ORDER BY p.created_at DESC LIMIT #{offset},10")
   List<PostDTO> findAll(@Param("offset") int offset);

   @Select("SELECT p.*, u.username FROM posts p JOIN users u ON p.user_id=u.id WHERE p.id = #{id}")
   PostDTO findById(Integer id);

   @Update("UPDATE posts SET title=#{title}, content=#{content} WHERE id=#{id} AND user_id=#{userId}")
   int update(PostDTO post);

   @Delete("DELETE FROM posts WHERE id=#{id} AND user_id=#{userId}")
   int delete(@Param("id") int id, @Param("userId") int userId);

   @Select("SELECT COUNT(*) FROM posts")
   int count();
}
