package com.dlb.userlogin.domain;

/**
 * 数量         amount      "100000000000000"
 * 发送者私钥    privateKey  "307702010104208bafad9eaf9aafe36c50309eeacf469e27eb4498ae1008f4f73dbcc09e5f44c3a00a06082a8648ce3d030107a14403420004327b2196e193b93d9dceb127ea73f74b021dd1a83d21fcf0999e261ada36e3139f3e9fd95b6edd236fe7f5075a99715130c3c2c29dd0d121199e394675d553e0"
 * 发送者地址    sender      "de5453f16541ffa8655a2b83b03cd91928888888"
 * 接收者地址    recipient   "b1dab07ed424171e0d8dfb9fcea5d17deb7d2315"
 */
public class RechargeBean {
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String amount;
    public String privateKey;
    public String sender;
    public String recipient;
    public int type; //充值类型
    public int user_id; //用户id

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String create_time;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
