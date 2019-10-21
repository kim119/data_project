package com.example.userlogin.controller;

import com.example.userlogin.Constant;
import com.example.userlogin.dao.UserMapper;
import com.example.userlogin.domain.*;
import com.example.userlogin.utils.AESUtil;
import com.example.userlogin.utils.UpdateHDFSUtils;
import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/")
    public String text() {
        return "test success";
    }

    @RequestMapping(value = "/register", produces = "application/json;charset=UTF-8")
    public JsonResult<String> register(String username, String password) {
        if (null == username || null == password) {
            return new JsonResult(Constant.ERRORCODE, " 用户名或密码不能为空");
        }
        try {
            List<User> users = userMapper.findUserByName(username);
            if (users.size() == 0) {
                User user = new User();
                user.setPassword(password);
                user.setUsername(username);
                userMapper.addUser(user);
            } else {
                return new JsonResult(Constant.ERRORCODE, " 该账号已存在");
            }
        } catch (Exception e) {
            return new JsonResult(Constant.ERRORCODE, " 后台维护中");
        }
        return new JsonResult();

    }

    /**
     * 登陆
     *
     * @return
     */
    @RequestMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public JsonResult<String> login(@RequestBody User user) {
        if (null == user.getUsername() || null == user.getPassword()) {
            return new JsonResult(Constant.ERRORCODE, " 用户名或密码不能为空");
        }
        String decPassword = null;
        try {
            decPassword = AESUtil.decrypt(user.getPassword(), AESUtil.KEY);
            System.out.println("decPassword" + decPassword);
            user.setPassword(decPassword);
            User user_server = userMapper.findUserAndPassord(user);
            if (null != user_server) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUsername(user_server.getUsername());
                userInfo.setUser_id(user_server.getId());
                return new JsonResult(userInfo);
            } else {
                return new JsonResult(Constant.ERRORCODE, "账号或密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(Constant.ERRORCODE, "账号或密码错误");
        }


    }


    /**
     * @param file     文件类型
     * @param username 上传的钱包地址或者公钥
     * @param dataType 上传数据的类型/影视或者其他
     * @return
     */
    @PostMapping("/upload")
    public JsonResult<String> updateZip(@RequestParam("file") MultipartFile file, String username, String dataType, int userId) {

        if (null == username || null == dataType || null == file) {
            return new JsonResult(Constant.ERRORCODE, "信息填入不完整");
        }
        User user = userMapper.findUserById(userId);
        if (user == null) {
            return new JsonResult(Constant.ERRORCODE, "用户id不存在");
        }
//        String data = Constant.getInstance().dataMap.get(dataType);
//        if (null == data) {
//            return new JsonResult(Constant.ERRORCODE, "数据类型不匹配");
//        }
        List<User> userByName = userMapper.findUserByName(username);
        if (userByName.size() == 0) {
            return new JsonResult(Constant.ERRORCODE, "用户名不存在");
        }
        File hh = UpdateHDFSUtils.MultipartFileToFile(file);
        try {
            UpdateHDFSUtils.copyfileToHdfs(dataType, username, new Configuration(), "hdfs://192.168.1.123:8020", hh);
        } catch (Exception e) {
            return new JsonResult(Constant.ERRORCODE, "上传失败");
        }
        if (file.getSize() / 1024 / 1024 < 0) {
            return new JsonResult(Constant.ERRORCODE, "上传失败，文件过于小");
        }
        String filename = file.getOriginalFilename();
        int i = filename.lastIndexOf(".");
        String newFile = "xx";
        if (i != -1) {
            newFile = filename.substring(i + 1);
        }
        UserUpInfoBean userUpInfoBean = new UserUpInfoBean();
        userUpInfoBean.setData_type(dataType);
        userUpInfoBean.setFile_format(newFile);
        userUpInfoBean.setFile_name(file.getOriginalFilename());
        userUpInfoBean.setFile_size(file.getSize() / 1024);
        userUpInfoBean.setUser_id(userId);
        userMapper.addUserUpInfo(userUpInfoBean);
        return new JsonResult();
    }

    /**
     * 获取用户上传记录
     */
    @RequestMapping(value = "/upinfo", produces = "application/json;charset=UTF-8")
    public JsonResult<String> getUpInfo(@RequestBody UserInfo user) {
        List<UserUpInfoBean> userUpInfos = userMapper.findUserUpInfo(user.getUser_id());
        return new JsonResult(userUpInfos);

    }


    /**
     * 返回图表
     *
     * @return JsonResult<String>
     */
    @RequestMapping(value = "/analyze", produces = "application/json;charset=UTF-8")
    public JsonResult<String> analyzeDataRecode(@RequestBody UserInfo user) {

        User userById = userMapper.findUserById(user.getUser_id());
        if (userById == null) {
            return new JsonResult(Constant.ERRORCODE, "用户id不存在");
        }
        List<MovieBean> top10Movie = userMapper.findTop10Movie(user.getUser_id());
        List<MovieTypeBean> movieType = userMapper.findMovieType(user.getUser_id());
        List<ProvinceBean> provinceData = userMapper.findProvinceData(user.getUser_id());
        AnalyzeDataResult analyzeDataResult = new AnalyzeDataResult();
        analyzeDataResult.setMovies(top10Movie);
        analyzeDataResult.setMovieTypes(movieType);
        analyzeDataResult.setProvinces(provinceData);
        //保存历史记录
        return new JsonResult(analyzeDataResult);

    }


}
