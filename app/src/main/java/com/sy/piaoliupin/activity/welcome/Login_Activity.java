package com.sy.piaoliupin.activity.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.sy.piaoliupin.MyApplication;
import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.dialog.Loading;
import com.sy.piaoliupin.entity.Save_Key;
import com.sy.piaoliupin.servlet.GetUserInfo_Servlet;
import com.sy.piaoliupin.servlet.Login_Servlet;
import com.sy.piaoliupin.utils.LogUtil;
import com.sy.piaoliupin.utils.SaveUtils;
import com.sy.piaoliupin.utils.TabToast;
import com.tencent.imsdk.TIMManager;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * @author: jiangyao
 * @date: 2018/5/18
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 登录
 */
public class Login_Activity extends Base_Activity implements View.OnClickListener, PlatformActionListener {
    private static final String TAG = "Login_Activity";

    LinearLayout wechat, qq;


    String loginType;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Login_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        initview();

        //判定是否有登录数据
        if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.OPENID))) {
            Loading.show(this, "登录中");

            new Login_Servlet(this).execute(SaveUtils.getString(Save_Key.S_登录类型), SaveUtils.getString(Save_Key.OPENID));

        }

    }

    private void initview() {

        wechat = findViewById(R.id.login_wechat);
        qq = findViewById(R.id.login_qq);

        wechat.setOnClickListener(this);
        qq.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Loading.show(this, "登录中");
        switch (view.getId()) {
            case R.id.login_wechat:
                loginType = "1";
                Login(Wechat.NAME);
                break;
            case R.id.login_qq:
                loginType = "2";
                Login(QQ.NAME);

                break;
        }
    }

    /**
     * 社会化登录
     *
     * @param name 平台名
     */
    public void Login(String name) {

        Platform plat = ShareSDK.getPlatform(name);
        plat.removeAccount(true); //移除授权状态和本地缓存，下次授权会重新授权
        plat.SSOSetting(false); //SSO授权，传false默认是客户端授权，没有客户端授权或者不支持客户端授权会跳web授权
        plat.setPlatformActionListener(this);//授权回调监听，监听oncomplete，onerror，oncancel三种状态
        if (plat.isClientValid()) {
            //判断是否存在授权凭条的客户端，true是有客户端，false是无
        }
        if (plat.isAuthValid()) {
            //判断是否已经存在授权状态，可以根据自己的登录逻辑设置
            TabToast.makeText("已经授权过了");
            return;
        }
//        plat.authorize();    //要功能，不要数据
        plat.showUser(null);    //要数据不要功能，主要体现在不会重复出现授权界面
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

        LogUtil.e(TAG, hashMap.toString());

        SaveUtils.setString(Save_Key.S_登录类型, loginType);
        MyApplication.userInfo.setLogintype(loginType);
        MyApplication.userInfo.setOpenid(platform.getDb().getUserId());
        MyApplication.userInfo.setNickname(String.valueOf(hashMap.get("nickname")));
        MyApplication.userInfo.setGender((String) hashMap.get("gender"));
        MyApplication.userInfo.setProvince((String) hashMap.get("province"));
        MyApplication.userInfo.setCity((String) hashMap.get("city"));
        MyApplication.userInfo.setFigureurl((String) hashMap.get("figureurl_qq_2"));

        new Login_Servlet(this).execute(loginType, platform.getDb().getUserId());

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        LogUtil.e(TAG, throwable.getMessage());

    }

    @Override
    public void onCancel(Platform platform, int i) {
        LogUtil.e(TAG, "onCancel");

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
