package com.dlb.userlogin.service;

import com.dlb.userlogin.Constant;
import com.dlb.userlogin.controller.UserController;
import com.dlb.userlogin.dao.UserMapper;
import com.dlb.userlogin.domain.*;
import com.dlb.userlogin.utils.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private TokenManager tokenManager;

    /**
     * 注册
     * @param userMap
     * @return
     */
    public JsonResult regist(Map<String, String> userMap) {
        String username = null;
        PayResultBean payResultBean = null;
        if (userMap.containsKey("username")) {
            username = userMap.get("username");
        }
        if (null == username) {
            return new JsonResult(Constant.ERRORCODE, "用户名不能为空");
        }
        try {
            List<User> users = userMapper.findUserByName(username);
            if (users.size() == 0) {
                User user = new User();
                user.setUsername(username);
                user.setPassword(Constant.defaultPass);
                //获取钱包地址
                synchronized (UserController.class) {
                    //把创建钱包的数据存入数据库 跟用户账号进行绑定
                    payResultBean = HttpsClient.getInstance().sendRequet(URLAdresss.URL_CREATWALLET, null);
                    if (null == payResultBean) {
                        return new JsonResult(Constant.ERROR_FAILECREATEWALLET, "创建钱包失败");
                    }

                    user.setAddress(payResultBean.getAddress());
                    user.setPrivateKey(payResultBean.getPrivateKey());
                    user.setPublicKey(payResultBean.getPublicKey());
                    userMapper.addUser(user);
                }

            } else {
                return new JsonResult(Constant.ERROR_ACCOUNTEXIST, " 该账号已存在");
            }
        } catch (Exception e) {
            return new JsonResult(Constant.ERRORCODE, " 后台维护中");
        }
        return new JsonResult();

    }

    /**
     * 登录
     * @param user
     * @return
     */
    public JsonResult login(User user) {
        if (null == user.getUsername() || null == user.getPassword()) {
            return new JsonResult(Constant.ERRORCODE, " 用户名或密码不能为空");
        }
        String decPassword = null;
        long balance = 0;
        try {
            decPassword = AESUtil.decrypt(user.getPassword(), AESUtil.KEY);
            user.setPassword(decPassword);
            List<User> user_server = userMapper.findUserAndPassord(user);
            System.out.println(user_server.toString());
            if (null != user_server&&user_server.size()==1) {

                String token = tokenManager.createToken(user_server.get(0).getUser_id());
                //获取用户余额
                try {

                } catch (Exception e) {

                    return new JsonResult(Constant.ERROR_FAILEGETBALANCE, "获取余额失败");
                }

                //封装数据
                UserInfo userInfo = new UserInfo();
                userInfo.setUsername(user_server.get(0).getUsername());
                userInfo.setUser_id(user_server.get(0).getUser_id());
                userInfo.setToken(token);
                userInfo.setAddress(user_server.get(0).getAddress());
                return new JsonResult(userInfo);
            } else {
                return new JsonResult(Constant.ERROR_ACCOUNTPASSWORDFALSE, "账号或密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(Constant.ERROR_ACCOUNTPASSWORDFALSE, "账户密码错误");
        }

    }


    public JsonResult updateZip(MultipartFile file, String username, String dataType, int userId, String token) {

        if (null == username || null == dataType || null == file) {
            return new JsonResult(Constant.ERRORCODE, "信息填入不完整");
        }

        //判断token
        if (!getToken()) {
            return new JsonResult(Constant.ERROT_TOKENINVALID, "token无效");
        }

        User user = userMapper.findUserById(userId);
        if (user == null) {
            return new JsonResult(Constant.ERROR_USERNOTEXIST, "用户不存在");
        }
//        String data = Constant.getInstance().dataMap.get(dataType);
//        if (null == data) {
//            return new JsonResult(Constant.ERRORCODE, "数据类型不匹配");
//        }

        List<User> userByName = userMapper.findUserByName(username);
        if (userByName.size() == 0) {
            return new JsonResult(Constant.ERROR_USERNOTEXIST, "用户名不存在");
        }
        if (file.getSize() / 1024 / 1024 < 0) {
            return new JsonResult(Constant.ERRORCODE, "上传失败，文件过于小");
        }
        File hh = UpdateHDFSUtils.MultipartFileToFile(file);
        try {

            long fileSize = file.getSize() / 1024 / 1024 == 0 ? 1 : file.getSize() / 1024 / 1024;
            UpdateHDFSUtils.copyfileToHdfs(dataType, user, new Configuration(), "hdfs://192.168.10.123:8020", hh);
            boolean isUp = upToQKL(user, fileSize + "");
            if (!isUp) {
                System.out.println("上传失败");
            }

        } catch (Exception e) {
            return new JsonResult(Constant.ERRORCODE, "上传失败");
        }

        String filename = file.getOriginalFilename();
        int i = filename.lastIndexOf(".");
        //默认格式
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
     * 转账
     * @param rechargeBean
     * @return
     */
    public JsonResult rechargeDLB(RechargeBean rechargeBean) {
        //判断token
        if (!getToken()) {
            return new JsonResult(Constant.ERROT_TOKENINVALID, "token无效");
        }
        //判断用户是否存在
        User user = userMapper.findUserById(rechargeBean.getUser_id());
        if (user == null) {
            return new JsonResult(Constant.ERROR_USERNOTEXIST, "用户不存在");
        }
        switch (rechargeBean.getType()) {
            //充值
            case Constant.CHARGETYPE:
                rechargeBean.setRecipient(user.getAddress());
                if (null == rechargeBean.getAmount() || null == rechargeBean.getSender() || null == rechargeBean.getPrivateKey() || null == rechargeBean.getRecipient()) {
                    return new JsonResult(Constant.ERRORCODE, "参数不完整");
                }
                if (sendTransferAccounts(rechargeBean))
                    return new JsonResult();
                break;
            //提现
            case Constant.WITHDRAWTYPE:
                rechargeBean.setPrivateKey(user.getPrivateKey());
                //rechargeBean.setRecipient(); //接收地址从客户端拿
                rechargeBean.setSender(user.getAddress());
                if (null == rechargeBean.getAmount() || null == rechargeBean.getSender() || null == rechargeBean.getPrivateKey() || null == rechargeBean.getRecipient()) {
                    return new JsonResult(Constant.ERRORCODE, "参数不完整");
                }
                if (sendTransferAccounts(rechargeBean)) return new JsonResult();
                break;
            case Constant.USERRECHARGE:
                if (null == rechargeBean.getAmount() || null == rechargeBean.getSender() || null == rechargeBean.getPrivateKey() || null == rechargeBean.getRecipient()) {
                    return new JsonResult(Constant.ERRORCODE, "参数不完整");
                }
                if (sendTransferAccounts(rechargeBean)) return new JsonResult();
                break;
        }
        return new JsonResult(Constant.ERRORCODE, "操作失败");

    }

    /**
     * 查询余额
     *
     * @param requestBean
     * @return
     */
    public JsonResult queryBalance(BalanceBean requestBean) {


        if (null == requestBean.getAddress()) {
            return new JsonResult(Constant.ERRORCODE, "钱包地址不能为空");
        }
        //判断token
        if (!getToken()) {
            return new JsonResult(Constant.ERROT_TOKENINVALID, "token无效");
        }
        long balance = getBalance(requestBean.getAddress());
        BalanceBean balanceBean = new BalanceBean();
        balanceBean.setBalance(balance);
        return new JsonResult(balanceBean);

    }

    /**
     * 执行余额获取请求
     *
     * @param address
     */
    public long getBalance(String address) {
        Map<String, String> params = new HashMap();
        params.put(WalletArgument.address, address);
        PayResultBean payResultBean = HttpsClient.getInstance().sendRequet(URLAdresss.URL_BALANCE, params);
        if (null == payResultBean) {
            return Long.getLong("0");
        } else {
            System.out.println(payResultBean.getBalance());
            return payResultBean.getBalance();
        }

    }

    /**
     * 执行转账操作请求
     *
     * @param rechargeBean
     * @return
     */
    private boolean sendTransferAccounts(@RequestBody RechargeBean rechargeBean) {
        boolean b = transferAccounts(rechargeBean);
        if (b) {
            return true;
        }
        return false;
    }

    /**
     * 转账
     */
    public boolean transferAccounts(RechargeBean rechargeBean) {
        Map<String, String> params = new HashMap();
        params.put(WalletArgument.sender, rechargeBean.getSender());
        params.put(WalletArgument.privateKey, rechargeBean.getPrivateKey());
        params.put(WalletArgument.amount, rechargeBean.getAmount());
        params.put(WalletArgument.recipient, rechargeBean.getRecipient());
        PayResultBean payResultBean = HttpsClient.getInstance().sendRequet(URLAdresss.URL_TRANSFERACCOUNTS, params);
        if (null == payResultBean) {
            return false;
        }
        if (payResultBean.getSuccess()) {
            //保存转账记录
            userMapper.saveRechargeRecord(rechargeBean);
            return true;

        }
        return false;

    }

    /**
     * 获取token
     */
    public boolean getToken() {
        String token = request.getHeader("token");
        boolean b = tokenManager.checkToken(token);
        return b;
    }

    /**
     * 使用数据 进行钱包支付
     */
    public boolean useDataToQKL(User user) {
        Map<String, String> params = new HashMap();
        params.put(WalletArgument.sender, user.getAddress());
        params.put(WalletArgument.privateKey, user.getPrivateKey());
        params.put(WalletArgument.dataType, "1");
        params.put(WalletArgument.dataAmount, 100 + "");
        try {
            HttpsClient.getInstance().sendRequet(URLAdresss.URL_USEDATE, params);
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 获取上传数据给区块链
     */
    public boolean upToQKL(User user, String fileSize) {
        Map<String, String> params = new HashMap();
        params.put(WalletArgument.sender, user.getAddress());
        params.put(WalletArgument.privateKey, user.getPrivateKey());
        params.put(WalletArgument.dataType, "1");
        params.put(WalletArgument.dataAmount, fileSize + "");
        PayResultBean payResultBean = HttpsClient.getInstance().sendRequet(URLAdresss.URL_UPLOADDATA, params);
        if (null == payResultBean) {
            return false;
        }
        if (payResultBean.getSuccess()) {
            return true;
        } else {
            return false;
        }

    }

}
