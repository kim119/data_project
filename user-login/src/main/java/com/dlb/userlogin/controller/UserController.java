package com.dlb.userlogin.controller;

import com.dlb.userlogin.Constant;
import com.dlb.userlogin.dao.UserMapper;
import com.dlb.userlogin.domain.*;
import com.dlb.userlogin.domain.record.MovieTypeRecord;
import com.dlb.userlogin.domain.record.ProvinceDataRecord;
import com.dlb.userlogin.domain.record.Top10MovieRecord;
import com.dlb.userlogin.service.TokenManager;
import com.dlb.userlogin.service.UserService;
import com.dlb.userlogin.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TokenManager tokenManager;

    @RequestMapping(value = "/register", produces = "application/json;charset=UTF-8")
    public JsonResult<String> register(@RequestBody Map<String, String> userMap) {
        return userService.regist(userMap);
    }

    /**
     * 登陆
     *
     * @return
     */
    @RequestMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public JsonResult<String> login(@RequestBody User user) {
        return userService.login(user);
    }


    /**
     * @param file     文件类型
     * @param username 上传的钱包地址或者公钥
     * @param dataType 上传数据的类型/影视或者其他
     * @return
     */
    @PostMapping("/upload")
    public JsonResult<String> updateZip(@RequestParam("file") MultipartFile file, String username, String dataType, int userId, String token) {
        return userService.updateZip(file, username, dataType, userId, token);
    }


    /**
     * 获取用户上传记录
     */
    @RequestMapping(value = "/upinfo", produces = "application/json;charset=UTF-8")
    public PageResult<String> getUpInfo(@RequestBody PageInfo pageInfo) {

        //第几页  ，每一第几条  总条数

        //2   5   10  //(5-10)  总条数/每页的条数 =页数    当前页*每的条数 10    //

        if (!getToken()) {
            return new PageResult(Constant.ERROT_TOKENINVALID, "token无效");
        }

        //当前条数
        //pageInfo.getCurrentPage()
        int count = userMapper.findUserUpInfoCount(pageInfo.getUser_id());
        int currentIndex = 0;

        if (pageInfo.getCurrentPage() == 0) {
            pageInfo.setCurrentPage(1);
        }
        currentIndex = pageInfo.getPageSize() * (pageInfo.getCurrentPage() - 1);

        pageInfo.setCurrentIndex(currentIndex);

        List<UserUpInfoBean> userUpInfos = userMapper.findUserUpInfo(pageInfo);
        return new PageResult(userUpInfos, count);

    }

    /**
     * 设置密码接口
     */
    @RequestMapping(value = "/setPayPW", produces = "application/json;charset=UTF-8")
    public JsonResult setPW(@RequestBody Map map) {
        //判断token
        if (!getToken()) {
            return new JsonResult(Constant.ERROT_TOKENINVALID, "token无效");
        }
        String decryptPayPW = null;
        String decryptNewPW = null;
        String user_id = (String) map.get("user_id");
        String pay_password = (String) map.get("pay_password");
        String newPassword = (String) map.get("newPassword");
        if (null == user_id || null == pay_password) {
            return new JsonResult(Constant.ERRORCODE, "参数不完整");
        }
        try {
            decryptPayPW = AESUtil.decrypt(pay_password, AESUtil.KEY);
            decryptNewPW = AESUtil.decrypt(newPassword, AESUtil.KEY);

        } catch (Exception e) {
            e.printStackTrace();
        }


        //用户是否已经存在
//        User user_01 = userMapper.findUserById(user.getUser_id());
        List<User> user_server = userMapper.findUsersById(Integer.parseInt(user_id));
        int isSet = user_server.get(0).getIsSet();
        if (user_server.size() > 0) {
            if (null != newPassword) {

                if (isSet == 0) {
                    //没有设置过密码
                    return new JsonResult(Constant.ERRORCODE, "请先设置支付密码");
                } else {
                    System.out.println("user_01" + user_server.get(0).toString());
                    if (!user_server.get(0).getPay_password().equals(decryptPayPW)) {
                        return new JsonResult(Constant.ERRORCODE, "原始密码不正确");
                    }
                    //设置过密码，进行修改密码
                    return sendChangeRequest(user_id, decryptNewPW, Constant.MODIFY_PAYPASSWORD);
                }
            } else {
                if (isSet == 1) {
                    return new JsonResult(Constant.ERRORCODE, "支付密码已经存在");
                }

                //设置支付密码
                return sendChangeRequest(user_id, decryptPayPW, Constant.SET_PAYPASSWORD);
            }
        } else {

            return new JsonResult(Constant.ERRORCODE, "用户不存在");
        }


    }

    /**
     * 修改密码和设置密码
     *
     * @param user_id
     * @param newPassword
     * @param type
     * @return
     */
    private JsonResult sendChangeRequest(String user_id, String newPassword, String type) {
        ChangePWBean changePWBean = new ChangePWBean();
        changePWBean.setUser_id(Integer.parseInt(user_id));
        changePWBean.setPay_password(newPassword);
        boolean b = false;
        switch (type) {
            case Constant.SET_PAYPASSWORD:
                b = userMapper.setPayPW(changePWBean);
                break;
            case Constant.MODIFY_PAYPASSWORD:
                b = userMapper.modifyPayPW(changePWBean);
                break;
        }
        if (b) {
            return new JsonResult();
        } else {
            return new JsonResult(Constant.ERRORCODE, "操作失败");
        }
    }


    /**
     * 数据结果获取
     *
     * @return JsonResult<String>
     */
    @RequestMapping(value = "/analyze", produces = "application/json;charset=UTF-8")
    public JsonResult<String> analyzeDataRecode(@RequestBody UserInfo user) {
        List<MovieBean> top10Movie = null;
        List<MovieTypeBean> movieType = null;
        List<ProvinceBean> provinceData = null;

        if (!getToken()) {
            return new JsonResult(Constant.ERROT_TOKENINVALID, "token无效");
        }
        List<User> usersById = userMapper.findUsersById(user.getUser_id());
        System.out.println(usersById.get(0).toString());

        if (usersById.size() == 0) {
            return new JsonResult(Constant.ERROR_USERNOTEXIST, "用户不存在");
        }

        if (null != user.getPay_password()) {
            //判断支付密码是否正确
            if (!user.getPay_password().equals(usersById.get(0).getPay_password())) {
                return new JsonResult(Constant.ERRORCODE, "支付密码错误");

            }
        }


        synchronized (UserController.class) {
            int money = 0;
            int dataCondition = user.getDataCondition();//0.代表1个月  ,1代表 半年 ,2代表一年
            switch (dataCondition) {
                case 0:
                    money = 1;
                    break;
                case 1:
                    money = 10;
                    break;
                case 2:
                    money = 100;
                    break;
            }

            boolean b = useDataToQKL(usersById.get(0), money);
            if (!b) {
                return new JsonResult(Constant.ERRORCODE, "dlb余额不足");
            }
        }

        switch (user.getDataCondition()) {
            case 0:
                top10Movie = userMapper.findOneTop10Movie(user.getUser_id());
                movieType = userMapper.findOneMovieType(user.getUser_id());
                provinceData = userMapper.findOneProvinceData(user.getUser_id());
                break;
            case 1:
                top10Movie = userMapper.findHalfTop10Movie(user.getUser_id());
                movieType = userMapper.findHalfMovieType(user.getUser_id());
                provinceData = userMapper.findHalfProvinceData(user.getUser_id());
                break;
            case 2:
                top10Movie = userMapper.findTop10Movie(user.getUser_id());
                movieType = userMapper.findMovieType(user.getUser_id());
                provinceData = userMapper.findProvinceData(user.getUser_id());
                break;
            default:

                break;
        }
        AnalyzeDataResult analyzeDataResult = new AnalyzeDataResult();
        analyzeDataResult.setId(user.getUser_id());
        analyzeDataResult.setMovies(top10Movie);
        analyzeDataResult.setMovieTypes(movieType);
        analyzeDataResult.setProvinces(provinceData);
        //保存查询记录
        saveQueryData(analyzeDataResult, user.getDataCondition());
        return new JsonResult(analyzeDataResult);

    }

    /**
     * 充值接口
     * 数量         amount      "100000000000000"
     * 发送者私钥    privateKey  "307702010104208bafad9eaf9aafe36c50309eeacf469e27eb4498ae1008f4f73dbcc09e5f44c3a00a06082a8648ce3d030107a14403420004327b2196e193b93d9dceb127ea73f74b021dd1a83d21fcf0999e261ada36e3139f3e9fd95b6edd236fe7f5075a99715130c3c2c29dd0d121199e394675d553e0"
     * 发送者地址    sender      "de5453f16541ffa8655a2b83b03cd91928888888"
     * 接收者地址    recipient   "b1dab07ed424171e0d8dfb9fcea5d17deb7d2315"
     */
    @RequestMapping(value = "/recharge", produces = "application/json;charset=UTF-8")
    public JsonResult rechargeDLB(@RequestBody RechargeBean rechargeBean) {
        return userService.rechargeDLB(rechargeBean);
    }

    /**
     * 充值记录
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/rechargeRecord", produces = "application/json;charset=UTF-8")
    public PageResult rechargeRecord(@RequestBody PageInfo pageInfo) {
        //判断token
        if (!getToken()) {
            return new PageResult(Constant.ERROT_TOKENINVALID, "token无效");
        }

        User user = userMapper.findUserById(pageInfo.getUser_id());
        if (user == null) {
            return new PageResult(Constant.ERROR_USERNOTEXIST, "用户不存在");
        }
        int currentIndex = 0;
        int count = userMapper.queryTopUpRecord(pageInfo.getUser_id());
        if (pageInfo.getCurrentPage() == 0) {
            pageInfo.setCurrentPage(1);
        }
        currentIndex = pageInfo.getPageSize() * (pageInfo.getCurrentPage() - 1);

        pageInfo.setCurrentIndex(currentIndex);

        List<RechargeBean> rechargeBeans = userMapper.queryTransferRecord(pageInfo);
        return new PageResult(rechargeBeans, count);
    }

    /**
     * 查询余额
     */
    @RequestMapping(value = "/queryBalance", produces = "application/json;charset=UTF-8")
    public JsonResult queryBalance(@RequestBody BalanceBean requestBean) {
        return userService.queryBalance(requestBean);
    }

    /**
     * 保存记录
     *
     * @param
     */
    private void saveQueryData(AnalyzeDataResult queryDatas, int type) {

        List<MovieTypeRecord> movieTypeRecords = new ArrayList();
        List<ProvinceDataRecord> provinceDataRecords = new ArrayList();
        List<Top10MovieRecord> top10MovieRecords = new ArrayList();
//
        List<MovieTypeBean> movieTypes = queryDatas.getMovieTypes();
        List<ProvinceBean> provinces = queryDatas.getProvinces();
        List<MovieBean> movies = queryDatas.getMovies();
//
//        for (int i = 0; i < movieTypes.size(); i++
//        ) {
//            MovieTypeRecord movieTypeRecord = new MovieTypeRecord();
//            movieTypeRecord.setName(movieTypes.get(i).getName());
//            movieTypeRecord.setNum(movieTypes.get(i).getNum());
//            movieTypeRecord.setUser_id(queryDatas.getId());
//            movieTypeRecord.setData_type(type);
//            movieTypeRecords.add(movieTypeRecord);
//        }
//
//
//        for (int i = 0; i < provinces.size(); i++
//        ) {
//            ProvinceDataRecord provinceDataRecord = new ProvinceDataRecord();
//            provinceDataRecord.setNum(provinces.get(i).getCount());
//            provinceDataRecord.setProvince(provinces.get(i).getProvince());
//            provinceDataRecord.setUser_id(queryDatas.getId());
//            provinceDataRecords.add(provinceDataRecord);
//
//        }
//
//        for (int i = 0; i < top10MovieRecords.size(); i++
//        ) {
//            Top10MovieRecord top10MovieRecord = new Top10MovieRecord();
//            top10MovieRecord.setNum(movies.get(i).getNum());
//            top10MovieRecord.setName(movies.get(i).getName());
//            top10MovieRecord.setUser_id(queryDatas.getId());
//            top10MovieRecord.setCid(movies.get(i).getCid());
//            top10MovieRecords.add(top10MovieRecord);
//
//        }
//        userMapper.saveAnalyzeMovieTypeRecode(movieTypeRecords);
//        userMapper.saveAnalyzeProvinceRecode(provinceDataRecords);
//        userMapper.saveAnalyzeMovieRecode(top10MovieRecords);
    }

    /**
     * 使用数据
     */
    public boolean useDataToQKL(User user, int money) {
        System.out.println(money);
        System.out.println(user.getAddress());
        Map<String, String> params = new HashMap();
        params.put(WalletArgument.sender, user.getAddress());
        params.put(WalletArgument.privateKey, user.getPrivateKey());
        params.put(WalletArgument.dataType, "1");
        params.put(WalletArgument.dataAmount, 1 + "");
        params.put(WalletArgument.pay, money + "");
        try {
            return HttpsClient.getInstance().sendRequet(URLAdresss.URL_USEDATE, params).getSuccess();
        } catch (Exception e) {
            return false;
        }

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
