package com.sy.piaoliupin.entity;

/**
 * @author: jiangyao
 * @date: 2018/5/23
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 登录返回
 */
public class Login_Entity extends Base_Entity {

    /**
     * uid : 1000021
     * password : 8ca3555b636709dd7ef10f8d3b64ece5
     * access_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJTSEEyNTYifQ__.eyJpYXQiOjE1MjcwNjY4OTAsImV4cCI6MTUyOTY1ODg5MCwidWlkIjoiMTAwMDAyMSJ9.8e6a80075b262e1240ada233875972ba281776a3a2c329d2bd50d1127910f107
     */

    private String uid;
    private String password;
    private String access_token;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
