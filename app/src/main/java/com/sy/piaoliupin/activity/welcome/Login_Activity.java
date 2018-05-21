package com.sy.piaoliupin.activity.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.activity.mine.UserInfo_Activity;
import com.sy.piaoliupin.service.Constants;
import com.sy.piaoliupin.service.TLSService;
import com.sy.piaoliupin.ui.HomeActivity;
import com.sy.piaoliupin.utils.LogUtil;
import com.sy.piaoliupin.utils.TabToast;

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
    private static final String TAG = "Login1_Activity";

    LinearLayout wechat, qq;

    private TLSService tlsService;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Login_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        tlsService = TLSService.getInstance();

        initview();

        findViewById(R.id.jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfo_Activity.start(Login_Activity.this);
                Login_Activity.this.finish();
            }
        });

    }

    private void initview() {

        wechat = findViewById(R.id.login_wechat);
        qq = findViewById(R.id.login_qq);

        wechat.setOnClickListener(this);
        qq.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_wechat:
                Login(Wechat.NAME);
                break;
            case R.id.login_qq:
                Login(QQ.NAME);
                break;
        }
    }

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

        TabToast.makeText("授权成功");
        LogUtil.e(TAG, platform.getDb().getUserId());
        LogUtil.e(TAG, hashMap.toString());

//        UserInfo_Activity.start(this);
//        finish();

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
}
