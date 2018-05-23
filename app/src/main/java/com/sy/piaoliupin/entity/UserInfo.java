package com.sy.piaoliupin.entity;

import com.sy.piaoliupin.utils.SaveUtils;

/**
 * @author: jiangyao
 * @date: 2018/5/22
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 社会化登录 个人信息
 */
public class UserInfo {
    /**
     * UID
     */
    private String UID;
    /**
     * OpenId
     */
    private String openid;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String figureurl;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 性别
     */
    private String gender;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 登录类型
     */
    private String logintype;
    /**
     * 校验
     */
    private String access_token;

    public String getLogintype() {
        return logintype;
    }

    public void setLogintype(String logintype) {
        SaveUtils.setString(Save_Key.S_登录类型, logintype);
        this.logintype = logintype;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        SaveUtils.setString(Save_Key.S_手机号, phone);
        this.phone = phone;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        SaveUtils.setString(Save_Key.OPENID, openid);
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        SaveUtils.setString(Save_Key.S_昵称, nickname);
        this.nickname = nickname;
    }

    public String getFigureurl() {
        return figureurl;
    }

    public void setFigureurl(String figureurl) {
        SaveUtils.setString(Save_Key.S_头像, figureurl);
        this.figureurl = figureurl;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        SaveUtils.setString(Save_Key.S_省份, province);
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        SaveUtils.setString(Save_Key.S_城市, city);
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        SaveUtils.setString(Save_Key.S_性别, gender);
        this.gender = gender;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        SaveUtils.setString(Save_Key.UID,UID);
        this.UID = UID;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        SaveUtils.setString(Save_Key.S_校验,access_token);
        this.access_token = access_token;
    }
}
