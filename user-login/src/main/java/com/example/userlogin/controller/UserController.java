package com.example.userlogin.controller;

import com.example.userlogin.Constant;
import com.example.userlogin.dao.UserMapper;
import com.example.userlogin.domain.JsonResult;
import com.example.userlogin.domain.User;
import com.example.userlogin.domain.UserInfo;
import com.example.userlogin.utils.UpdateHDFSUtils;
import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;


    //注册
    @RequestMapping("/register")
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
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/login")
    public JsonResult<String> login(String username, String password) {

        if (null == username || null == password) {
            return new JsonResult(Constant.ERRORCODE, " 用户名或密码不能为空");
        }
        User user = new User();
        user.setPassword(password);
        user.setUsername(username);
        User user_server = userMapper.findUserAndPassord(user);
        if (null != user_server) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(user_server.getUsername());
            return new JsonResult(userInfo);
        } else {
            return new JsonResult(Constant.ERRORCODE, "登陆失败");

        }

    }

    /**
     * @param file     文件类型
     * @param username 上传的钱包地址或者公钥
     * @param dataType 上传数据的类型/影视或者其他
     * @return
     */
    @PostMapping("/upload")
    public JsonResult<String> updateZip(@RequestParam("file") MultipartFile file, String username, String dataType) {

        if (null == username || null == dataType || null == file) {
            return new JsonResult(Constant.ERRORCODE, "信息填入不完整");
        }
        String data = Constant.getInstance().dataMap.get(dataType);
        if (null == data) {
            return new JsonResult(Constant.ERRORCODE, "数据类型不匹配");
        }
        List<User> userByName = userMapper.findUserByName(username);
        if (userByName.size() == 0) {
            return new JsonResult(Constant.ERRORCODE, "用户名不存在");
        }
        File hh = UpdateHDFSUtils.MultipartFileToFile(file);
        try {
            UpdateHDFSUtils.copyfileToHdfs(data, username, new Configuration(), "hdfs://192.168.1.123:8020", hh);


        } catch (Exception e) {
            return new JsonResult(Constant.ERRORCODE,"上传失败");
        }
        return new JsonResult();
    }


}
