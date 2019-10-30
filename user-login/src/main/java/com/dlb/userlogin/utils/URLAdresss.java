package com.dlb.userlogin.utils;

/**
 * 支付接口
 */
public class URLAdresss {

    public static final String BASE_URL = "https://192.168.10.111:13091/api";
    //创建钱包
    public static final String URL_CREATWALLET = "/wallet";
    //转账
    public static final String URL_TRANSFERACCOUNTS = "/transaction/launch/transafer";
    //上传数据
    public static final String URL_UPDATEDATE = "/upload/launch/transafer";
    //使用数据
    public static final String URL_USEDATE = "/useData/launch/transafer";


}
