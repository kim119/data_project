package com.dlb.userlogin.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用支付接口类
 */
public class TestHttps {

    private static class SingletonClassInstance {
        private static final TestHttps instance = new TestHttps();
    }

    private TestHttps() {
    }

    public static TestHttps getInstance() {
        return SingletonClassInstance.instance;
    }

    public static void main(String[] args) {
        Map<String,String> map=new HashMap<>();
        map.put("sender","b1dab07ed424171e0d8dfb9fcea5d17deb7d2315");
        map.put("privateKey","307702010104208cb494a9a8dfd1af59743a01c08a86fbaea34e23f6ca85328bec2753c3489c16a00a06082a8648ce3d030107a14403420004134399d7cde624c1e37f34613fd07cad910cd879764434779a12444cf4554a3011634e0be1af4cfc6caaeb5117fdba5fdaf9139b6c50f7bcb35ae20c3f720aab");
        map.put("dataType","1");
        map.put("dataAmount","100");
        String s = getInstance().uploadData(URLAdresss.URL_UPDATEDATE, map);
        System.out.println(s);
    }

    /**
     * 创建钱包
     *
     * @param url    请求地址
     * @param params 请求参数
     */
    public String creatWallt(String url, Map params) {
        String s = new HttpClientUtil().doPost(URLAdresss.BASE_URL + url, params, "utf-8");
        return s;
    }

    /**
     * 转账
     *
     * @param url
     * @param params
     */
    public String transaferAccounts(String url, Map params) {
        String s = new HttpClientUtil().doPost(URLAdresss.BASE_URL + url, params, "utf-8");
        return s;
    }

    /**
     * 上传数据
     * @param url
     * @param params
     */
    public String uploadData(String url, Map params) {
        String s = new HttpClientUtil().doPost(URLAdresss.BASE_URL + url, params, "utf-8");
        return s;
    }

    /**
     * 使用数据
     * @param url
     * @param params
     * @return
     */
    public String useData(String url,Map params){
        String s = new HttpClientUtil().doPost(URLAdresss.BASE_URL + url, params, "utf-8");
        return s;

    }


//    public void sendRequest() throws Exception {
//        HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
//        SSLContext sc = SSLContext.getInstance("TLS");
//        sc.init(null, trustAllCerts, new SecureRandom());
//        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//        URL url = new URL(
//                "https://192.168.10.111:13091/api/wallet");
//        // 打开restful链接
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("POST");// POST GET PUT DELETE
//        // 设置访问提交模式，表单提交
//        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
//        conn.setConnectTimeout(130000);// 连接超时 单位毫秒
//        conn.setReadTimeout(130000);// 读取超时 单位毫秒
//        // 读取请求返回值
//        byte bytes[] = new byte[1024];
//        InputStream inStream = conn.getInputStream();
//        inStream.read(bytes, 0, inStream.available());
//        System.out.println(new String(bytes, "utf-8"));
//    }
//
//    static TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//        @Override
//        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//            // TODO Auto-generated method stub
//        }
//
//        @Override
//        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//            // TODO Auto-generated method stub
//        }
//
//        @Override
//        public X509Certificate[] getAcceptedIssuers() {
//            // TODO Auto-generated method stub
//            return null;
//        }
//    }};

}
