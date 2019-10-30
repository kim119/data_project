package com.dlb.userlogin.utils;

import com.alibaba.fastjson.JSONObject;
import com.dlb.userlogin.utils.SSLClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.util.Iterator;
import java.util.Map;

public class HttpClientUtil {

    public String doPost(String url, Map<String, String> paramsMap, String charset) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            // json方式
            JSONObject jsonParam = new JSONObject();
            if(paramsMap!=null) {
                // 设置参数
                Iterator<?> iterator = paramsMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    @SuppressWarnings("unchecked")
                    Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                    jsonParam.put(elem.getKey(), elem.getValue());
                }
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}