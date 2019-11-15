package com.dlb.userlogin.utils;

/**
 * 支付接口
 */
public class URLAdresss {

    public static final String BASE_URL = "https://192.168.10.171:13091";
    //创建钱包
    public static final String URL_CREATWALLET = "/api/wallet";
    //转账
    public static final String URL_TRANSFERACCOUNTS = "/api/transaction/launch/transafer";
    //上传数据
    public static final String URL_UPLOADDATA = "/api/upload/launch/transafer";
    //使用数据
    public static final String URL_USEDATE = "/api/useData/launch/transafer";
    //查询余额
    public static final String URL_BALANCE = "/api/balance";


}
