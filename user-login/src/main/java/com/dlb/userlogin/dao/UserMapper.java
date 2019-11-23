package com.dlb.userlogin.dao;

import com.dlb.userlogin.domain.*;
import com.dlb.userlogin.domain.record.MovieTypeRecord;
import com.dlb.userlogin.domain.record.ProvinceDataRecord;
import com.dlb.userlogin.domain.record.Top10MovieRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserMapper {

    User findUserById(Integer id);

    List<User> findUsersById(Integer id);

    //通过用户名查询用户
    List<User> findUserByName(String username);

    //增加新用户
    boolean addUser(User user);

    //找到用户
    List<User> findUserAndPassord(User user);

    //插入上传文件信息
    boolean addUserUpInfo(UserUpInfoBean userUpInfoBean);

    //获取用户上传记录
    List<UserUpInfoBean> findUserUpInfo(PageInfo pageInfo);

    //修改密码
    boolean updatePassword(User user);

    //查询所有电影前10的数据
    public List<MovieBean> findTop10Movie(long id);

    //查询一个月数据 的电影前10的数据
    public List<MovieBean> findOneTop10Movie(long id);

    //查询半年数据  的电影前10的数据
    public List<MovieBean> findHalfTop10Movie(long id);

    //获取各省的用户
    public List<ProvinceBean> findProvinceData(long id);

    public List<ProvinceBean> findOneProvinceData(long id);

    public List<ProvinceBean> findHalfProvinceData(long id);


    //获取前10最受欢迎的视屏类型
    public List<MovieTypeBean> findMovieType(long id);

    public List<MovieTypeBean> findOneMovieType(long id);

    public List<MovieTypeBean> findHalfMovieType(long id);

    public void saveAnalyzeMovieRecode(List<Top10MovieRecord> top10MovieRecords);

    public void saveAnalyzeMovieTypeRecode(List<MovieTypeRecord> movieTypeRecord);

    public void saveAnalyzeProvinceRecode(List<ProvinceDataRecord> provinceDataRecord);

    public void saveRechargeRecord(RechargeBean rechargeBean);

    //查询交易记录
    public List<RechargeBean> queryTransferRecord(PageInfo pageInfo);

    public User queryUserAndPassword(User user);

    public boolean setPayPW(ChangePWBean user);

    public boolean modifyPayPW(ChangePWBean user);

    /*获取总条数*/
    public int findUserUpInfoCount(long user_id);

    /*获取充值记录总条数*/
    public int queryTopUpRecord(long user_id);


}

