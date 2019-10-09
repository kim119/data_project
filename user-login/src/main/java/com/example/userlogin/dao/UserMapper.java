package com.example.userlogin.dao;

import com.example.userlogin.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    public User findUserById(int id);

    //通过用户名查询用户
    public List<User> findUserByName(String username);

    //增加新用户
    public boolean addUser(User user);

    public User  findUserAndPassord(User user);
}

