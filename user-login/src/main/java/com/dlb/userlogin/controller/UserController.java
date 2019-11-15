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
    public JsonResult<String> getUpInfo(@RequestBody UserInfo user) {

        if (!getToken()) {
            return new JsonResult(Constant.ERROT_TOKENINVALID, "token无效");
        }
        List<UserUpInfoBean> userUpInfos = userMapper.findUserUpInfo(user.getUser_id());
        return new JsonResult(userUpInfos);

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
        User userById = userMapper.findUserById(user.getUser_id());

        if (userById == null) {
            return new JsonResult(Constant.ERROR_USERNOTEXIST, "用户不存在");
        }
        synchronized (UserController.class) {
            boolean b = useDataToQKL(userById);
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
     * @param userInfo
     * @return
     */
    @RequestMapping(value = "/rechargeRecord", produces = "application/json;charset=UTF-8")
    public JsonResult rechargeRecord(@RequestBody UserInfo userInfo) {
        //判断token
        if (!getToken()) {
            return new JsonResult(Constant.ERROT_TOKENINVALID, "token无效");
        }

        User user = userMapper.findUserById(userInfo.getUser_id());
        if (user == null) {
            return new JsonResult(Constant.ERROR_USERNOTEXIST, "用户不存在");
        }
        List<RechargeBean> rechargeBeans = userMapper.queryTransferRecord(userInfo.getUser_id());
        return new JsonResult(rechargeBeans);
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

        List<MovieTypeBean> movieTypes = queryDatas.getMovieTypes();
        List<ProvinceBean> provinces = queryDatas.getProvinces();
        List<MovieBean> movies = queryDatas.getMovies();

        for (int i = 0; i < movieTypes.size(); i++
        ) {
            MovieTypeRecord movieTypeRecord = new MovieTypeRecord();
            movieTypeRecord.setName(movieTypes.get(i).getName());
            movieTypeRecord.setNum(movieTypes.get(i).getNum());
            movieTypeRecord.setUser_id(queryDatas.getId());
            movieTypeRecord.setData_type(type);
            movieTypeRecords.add(movieTypeRecord);
        }


        for (int i = 0; i < provinces.size(); i++
        ) {
            ProvinceDataRecord provinceDataRecord = new ProvinceDataRecord();
            provinceDataRecord.setNum(provinces.get(i).getCount());
            provinceDataRecord.setProvince(provinces.get(i).getProvince());
            provinceDataRecord.setUser_id(queryDatas.getId());
            provinceDataRecords.add(provinceDataRecord);

        }

        for (int i = 0; i < top10MovieRecords.size(); i++
        ) {
            Top10MovieRecord top10MovieRecord = new Top10MovieRecord();
            top10MovieRecord.setNum(movies.get(i).getNum());
            top10MovieRecord.setName(movies.get(i).getName());
            top10MovieRecord.setUser_id(queryDatas.getId());
            top10MovieRecord.setCid(movies.get(i).getCid());
            top10MovieRecords.add(top10MovieRecord);

        }
        userMapper.saveAnalyzeMovieTypeRecode(movieTypeRecords);
        userMapper.saveAnalyzeProvinceRecode(provinceDataRecords);
        userMapper.saveAnalyzeMovieRecode(top10MovieRecords);
    }

    /**
     * 使用数据
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
     * 获取token
     */
    public boolean getToken() {
        String token = request.getHeader("token");
        boolean b = tokenManager.checkToken(token);
        return b;
    }

}
