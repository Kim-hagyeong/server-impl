package com.example.serverimpl.mapper;

import com.example.serverimpl.dto.UserDTO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO users(username, password, email) VALUES(#{username}, #{password}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(UserDTO user);

    @Select("SELECT * FROM users WHERE username = #{username}")
    UserDTO findByUsername(String username);

    @Select("SELECT * FROM users WHERE id = #{id}")
    UserDTO findById(Integer id);

    @Update("UPDATE users SET email = #{email}, password = #{password} WHERE id = #{id}")
    void update(UserDTO user);
}
