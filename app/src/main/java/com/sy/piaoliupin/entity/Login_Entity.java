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
     * access_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJTSEEyNTYifQ__.eyJpYXQiOjE1MjcxNDkzODMsImV4cCI6MTUyOTc0MTM4MywidWlkIjoiMTAwMDAyMSJ9.ccd005fe72734f5c34c1807bf12481c54acfd1b97c4752529ecc1625200eb95c
     * usersig : eJxl0E9rgzAYx-G7r0Jy3Zj506y1sIMToUW3MTpt2UWiiSW4akwTcYy993Y6mLDz5wsPv*fLcV0XvCW7O1aWrW1Mbj6VAO7aBRDc-qFSkufM5ETzfygGJbXIWWWEHhFTH0M4TyQXjZGV-A3QVSFGs*DM63w8Mvni6isfETxP5HHEpygNt9Hee91a3QmWDKkpOGJkU2xg1t3ce6UuxSHqY9wpTKkNZBRQa5P3j9gLTDz0dV3xNOyOXchl8ZgVVfZsX04777AknLYPs5NGnqZfIIqXaOGTFZlpL-RZts00GSKKEPJ-hgHn27kArENcug__
     */

    private String uid;
    private String access_token;
    private String usersig;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUsersig() {
        return usersig;
    }

    public void setUsersig(String usersig) {
        this.usersig = usersig;
    }
}
