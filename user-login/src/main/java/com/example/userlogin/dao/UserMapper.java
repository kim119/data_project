package com.example.userlogin.dao;

import com.example.userlogin.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    User findUserById(int id);

    //通过用户名查询用户
    List<User> findUserByName(String username);

    //增加新用户
    boolean addUser(User user);

    //找到用户
    User findUserAndPassord(User user);

    //插入上传文件信息
    boolean addUserUpInfo(UserUpInfoBean userUpInfoBean);

    //获取用户上传记录
    List<UserUpInfoBean> findUserUpInfo(int id);

    //修改密码
    boolean updatePassword(User user);

    //查询所有电影前10的数据
    public List<MovieBean> findTop10Movie(int id);

    //查询一个月数据 的电影前10的数据
    public List<MovieBean> findOneTop10Movie(int id);
    //查询半年数据  的电影前10的数据
    public List<MovieBean> findHalfTop10Movie(int id);

    //获取各省的用户
    public List<ProvinceBean> findProvinceData(int id);

    public List<ProvinceBean> findOneProvinceData(int id);

    public List<ProvinceBean> findHalfProvinceData(int id);




    //获取前10最受欢迎的视屏类型
    public List<MovieTypeBean> findMovieType(int id);

    public List<MovieTypeBean> findOneMovieType(int id);

    public List<MovieTypeBean> findHalfMovieType(int id);


}

