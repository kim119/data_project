package com.dlb.userlogin.controller;

import com.dlb.userlogin.Constant;
import com.dlb.userlogin.dao.UserInfoMapper;
import com.dlb.userlogin.dao.UserMapper;
import com.dlb.userlogin.domain.JsonResult;
import com.dlb.userlogin.domain.PageInfo;
import com.dlb.userlogin.domain.User;
import com.dlb.userlogin.domain.dlb.DlB_User;
import com.dlb.userlogin.service.TokenManager;
import com.dlb.userlogin.utils.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userinfo")
@CrossOrigin
public class UserCenterController {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    HttpServletRequest request;

    @RequestMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public JsonResult login(@RequestBody Map<String, String> map) {
        if (map.containsKey("username") && map.containsKey("password")) {
            String username = map.get("username");
            String password = map.get("password");
            DlB_User user = userInfoMapper.queryUser(username, password);
            if (null != user) {
                //生成token
                String token = tokenManager.createToken(user.getId());
                user.setToken(token);
                return new JsonResult(user);
            }
        }
        return new JsonResult(Constant.ERRORCODE, "用户名或密码错误");
    }

    /*获取所有的用户信息*/
    @RequestMapping(value = "/getUsers")
    public JsonResult getAllUsers(@RequestBody PageInfo pageInfo) {
        if (!getToken()) {
            return new JsonResult(Constant.ERROT_TOKENINVALID, "token无效");
        }
        List<com.dlb.userlogin.domain.User> users = userInfoMapper.queryAllUsers();
        return new JsonResult(users);
    }

    /*修改密码*/
    @RequestMapping(value = "modifyPassword", produces = "application/json;charset=UTF-8")
    public JsonResult modifyPassword(@RequestBody com.dlb.userlogin.domain.User user) {
        String decryptPW = null;
        String decryptNewPW = null;
        if (!getToken()) {
            return new JsonResult(Constant.ERROT_TOKENINVALID, "token无效");
        }

        try {
            decryptPW = AESUtil.decrypt(user.getPassword(), AESUtil.KEY);
            decryptNewPW = AESUtil.decrypt(user.getNewPassword(), AESUtil.KEY);
            System.out.println("aaa"+decryptNewPW);
            System.out.println("bb"+decryptPW);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (decryptNewPW.length() < 6) {

            return new JsonResult(Constant.ERRORCODE, "密码不能小于6位");
        }
        user.setPassword(decryptPW);
        List<User> userAndPassord = userMapper.findUserAndPassord(user);
        if (userAndPassord.size() <= 0) {
            return new JsonResult(Constant.ERRORCODE, "密码不正确");
        }
        user.setNewPassword(decryptNewPW);
        boolean b = userInfoMapper.modifyPassword(user);

        if (!b) {
            return new JsonResult(Constant.ERRORCODE, "修改密码失败");
        }
        return new JsonResult();
    }

    /**
     * 删除用户
     */
    @RequestMapping(value = "deleteUser", produces = "application/json;charset=UTF-8")
    public JsonResult deleteUser(@RequestBody com.dlb.userlogin.domain.User user) {
        if (!getToken()) {
            return new JsonResult(Constant.ERROT_TOKENINVALID, "token无效");
        }
        boolean b = userInfoMapper.deleteUser(user);
        if (!b) {
            return new JsonResult(Constant.ERRORCODE, "删除用户失败");
        }
        return new JsonResult();

    }

    /**
     * 获取token
     */
    public boolean getToken() {
        String token = request.getHeader("token");
        boolean b = tokenManager.checkToken(token);
        return b;
    }

}
