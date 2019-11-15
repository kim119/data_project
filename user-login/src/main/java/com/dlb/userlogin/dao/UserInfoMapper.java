package com.dlb.userlogin.dao;

import com.dlb.userlogin.domain.dlb.DlB_User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserInfoMapper {

    /*用户平台*/
    public DlB_User queryUser(String username, String password);
    /**
     * 获取所有的用户
     */
    public List<com.dlb.userlogin.domain.User> queryAllUsers();
    /**
     * 修改密码
     */
    public boolean modifyPassword(com.dlb.userlogin.domain.User user);

    /**
     * 删除用户
     */
    public boolean deleteUser(com.dlb.userlogin.domain.User user);
}
