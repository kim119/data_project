package com.dlb.userlogin.utils;

import com.alibaba.fastjson.JSONObject;
import com.dlb.userlogin.Constant;
import com.dlb.userlogin.domain.PayResultBean;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用支付接口类
 */
public class HttpsClient {

    private static class SingletonClassInstance {
        private static final HttpsClient instance = new HttpsClient();
    }

    private HttpsClient() {
    }

    public static HttpsClient getInstance() {
        return SingletonClassInstance.instance;
    }

    public static void main(String[] args) {

        Map<String, String> params = new HashMap();
        params.put(WalletArgument.sender, "b1dab07ed424171e0d8dfb9fcea5d17deb7d2315");
        params.put(WalletArgument.privateKey, "307702010104208cb494a9a8dfd1af59743a01c08a86fbaea34e23f6ca85328bec2753c3489c16a00a06082a8648ce3d030107a14403420004134399d7cde624c1e37f34613fd07cad910cd879764434779a12444cf4554a3011634e0be1af4cfc6caaeb5117fdba5fdaf9139b6c50f7bcb35ae20c3f720aab");
        params.put(WalletArgument.dataType, "1");
        params.put(WalletArgument.dataAmount, "1");
        //  PayResultBean useData = getInstance().sendRequet(URLAdresss.URL_CREATWALLET, null, Constant.CREATEWALLT);
        PayResultBean useData = getInstance().sendRequet(URLAdresss.URL_USEDATE, params);
        Boolean success = useData.getSuccess();
        System.out.println(success);

    }

    /**
     * @param url    地址
     * @param params 参数
     * @return
     */
    public PayResultBean sendRequet(String url, Map params) {

        String s = new HttpClientUtils().doPost(URLAdresss.BASE_URL + url, params, "utf-8");
        System.out.println(s);
        if (null != s)
            return parseData(s, url);
        else
            return null;
    }

    /**
     * 解析数据
     *
     * @param data 数据
     * @return
     */
    public PayResultBean parseData(String data, String type) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        Map resultParams = new HashMap();

        switch (type) {

            case URLAdresss
                    .URL_CREATWALLET:
                resultParams.put(WalletArgument.address, jsonObject.getString(WalletArgument.address));
                resultParams.put(WalletArgument.privateKey, jsonObject.getString(WalletArgument.privateKey));
                resultParams.put(WalletArgument.publicKey, jsonObject.getString(WalletArgument.publicKey));
                break;

            case URLAdresss.URL_TRANSFERACCOUNTS:
                resultParams.put(WalletArgument.success, jsonObject.getBoolean(WalletArgument.success));
                resultParams.put(WalletArgument.transactionHash, jsonObject.getString(WalletArgument.transactionHash));
                resultParams.put(WalletArgument.publicKey, jsonObject.getString(WalletArgument.publicKey));
                break;

            case URLAdresss.URL_UPLOADDATA:
                resultParams.put(WalletArgument.success, jsonObject.getBoolean(WalletArgument.success));
                resultParams.put(WalletArgument.transactionHash, jsonObject.getString(WalletArgument.transactionHash));
                resultParams.put(WalletArgument.reward, jsonObject.getString(WalletArgument.reward));
                break;

            case URLAdresss.URL_USEDATE:
                resultParams.put(WalletArgument.success, jsonObject.getBoolean(WalletArgument.success));
                resultParams.put(WalletArgument.transactionHash, jsonObject.getString(WalletArgument.success));
                resultParams.put(WalletArgument.reward, jsonObject.getString(WalletArgument.reward));
                break;

            case URLAdresss.URL_BALANCE:
                resultParams.put(WalletArgument.success, jsonObject.getBoolean(WalletArgument.success));
                resultParams.put(WalletArgument.balance, jsonObject.getLong(WalletArgument.balance));
                break;

        }
        PayResultBean prb = new PayResultBean();
        try {
            BeanUtils.copyProperties(prb, resultParams);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return prb;

    }


}
