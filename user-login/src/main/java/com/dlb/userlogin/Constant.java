package com.dlb.userlogin;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量的定义
 */
public class Constant {

    //成功按钮
    public static final String SUCCESS = "10000";
    //登陆失败
    public static final String ERRORCODE = "10001";
    public static final String ERROT_TOKENINVALID = "10002"; //token 无效
    public static final String ERROR_FAILEGETBALANCE = "50003";
    public static final String ERROR_FAILECREATEWALLET = "50004";
    public static final String ERROR_ACCOUNTEXIST = "10003";
    public static final String ERROR_ACCOUNTPASSWORDFALSE = "10004";
    public static final String ERROR_USERNOTEXIST = "10005"; //用户不存在

    public static final String defaultPass = "dlb123456";

    //数据类型

    public Map<String, String> dataMap = new HashMap();

    //使用单例设计模式
    private static class SingletonClassInstance {
        private static final Constant instance = new Constant();
    }

    private Constant() {
        dataMap.put("影视音影", "movie");
        dataMap.put("健康医疗", "health");
        dataMap.put("新闻/广告", "news");
        dataMap.put("其他", "other");
    }

    public final static String CREATEWALLT = "createWallt"; //创建钱包
    public final static String TRANSAFERACCOUNTS = "transaferAccounts"; //转账
    public final static String UPLOADDATA = "uploadData"; //上传数据
    public final static String USEDATA = "useData";//使用数据
    public final static String BALANCE = "balance";//使用数据
    public final static long TOKEN_EXPIRE_HOUR = 24;//使用数据

    public final static int CHARGETYPE = 1; //充值
    public final static int WITHDRAWTYPE = 2; //充值
    public final static int USERRECHARGE = 3; //充值
    public final static String SET_PAYPASSWORD = "SET_PAYPASSWORD"; //设置支付密码
    public final static String MODIFY_PAYPASSWORD = "MODIFY_PAYPASSWORD"; //修改支付密码

    public static Constant getInstance() {
        return SingletonClassInstance.instance;
    }

}
