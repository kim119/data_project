package com.dlb.userlogin.domain;

public class PageResult<T> {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String code;
    private String msg;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    private int total;

    /**
     * 若没有数据返回，默认状态码为 0，提示信息为“操作成功！”
     */
    public PageResult() {
        this.code = "0";
        this.msg = "操作成功";
    }

    /**
     * 若没有数据返回，可以人为指定状态码和提示信息
     *
     * @param code
     * @param msg
     */
    public PageResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    /**
     * 有数据返回时，状态码为 0，默认提示信息为“操作成功！”
     *
     * @param data
     */
    public PageResult(T data,int total) {
        this.data = data;
        this.code = "0";
        this.msg = "请求成功";
        this.total=total;


    }

}
