package com.sy.piaoliupin.service;

import android.content.Context;

import com.tencent.qcloud.sdk.Constant;

/**
 * 初始化tls登录模块
 */
public class TlsBusiness {


    private TlsBusiness() {
    }

    public static void init(Context context) {
        TLSConfiguration.setSdkAppid(Constant.SDK_APPID);
        TLSConfiguration.setAccountType(Constant.ACCOUNT_TYPE);
        TLSConfiguration.setTimeout(8000);
        TLSConfiguration.setQqAppIdAndAppKey("1106094709", "cfDHm3UDQiPYVjVo");
        TLSConfiguration.setWxAppIdAndAppSecret("wx65f71c2ea2b122da", "1d30d40f8db6d3ad0ee6492e62ad5d57");
        TLSService.getInstance().initTlsSdk(context);
    }

    public static void logout(String id) {
        TLSService.getInstance().clearUserInfo(id);

    }
}
