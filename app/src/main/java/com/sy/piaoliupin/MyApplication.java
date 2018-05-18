package com.sy.piaoliupin;

import android.app.Application;
import android.content.Context;

import com.sy.piaoliupin.servlet.GetMessage_Servlet;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushListener;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.sy.piaoliupin.utils.Foreground;


/**
 * 全局Application
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    private static Context context;

    public static Context getInstance() {
        return context;
    }

    public static boolean LogShow = true;

    @Override
    public void onCreate() {
        super.onCreate();
        Foreground.init(this);
        context = getApplicationContext();
        if (MsfSdkUtils.isMainProcess(this)) {
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify) {
                        //消息被设置为需要提醒
                        notification.doNotify(getApplicationContext(), R.mipmap.logo);
                    }
                }
            });
        }

    }

    public static Context getContext() {
        return context;
    }


}
