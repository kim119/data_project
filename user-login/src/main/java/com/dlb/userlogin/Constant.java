package com.dlb.userlogin;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量的定义
 */
public class Constant {

    //登陆失败
    public static final String ERRORCODE = "10001";

    //数据类型

    public  Map<String, String> dataMap = new HashMap();

    //使用单例设计模式
    private static class SingletonClassInstance {
        private static final Constant instance = new Constant();
    }
    private Constant() {
        dataMap.put("影视音影","movie");
        dataMap.put("健康医疗","health");
        dataMap.put("新闻/广告","news");
        dataMap.put("其他","other");
    }

    public static Constant getInstance() {
        return SingletonClassInstance.instance;
    }

}
