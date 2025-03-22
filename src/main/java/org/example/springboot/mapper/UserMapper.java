package org.example.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.springboot.pojo.User;

import java.util.List;


@Mapper
public interface UserMapper {
    User findByUsernameAndPassword(String username, String password);
    int insertUser(User user);
    User findByUsername(String username);
    List<User> getUser();
    int updateUser(User user);
}

//@Mapper
//public interface UserMapper {
//
//    @Select("select * from user where username=#{username} and password=#{password}")
//    User findByUsernameAndPassword(String username, String password);
//
//    @Insert("INSERT INTO user (username, password) VALUES (#{username}, #{password})")
//    int insertUser(User user);
//
//
//}