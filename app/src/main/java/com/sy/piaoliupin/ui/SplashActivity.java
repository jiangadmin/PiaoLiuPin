package com.sy.piaoliupin.ui;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.huawei.android.pushagent.PushManager;
import com.meizu.cloud.pushsdk.util.MzSystemUtils;
import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.activity.welcome.Guide_Activity;
import com.sy.piaoliupin.activity.welcome.LoginForPWD_Activity;
import com.sy.piaoliupin.activity.welcome.Login_Activity;
import com.sy.piaoliupin.business.InitBusiness;
import com.sy.piaoliupin.business.LoginBusiness;
import com.sy.piaoliupin.entity.Save_Key;
import com.sy.piaoliupin.event.FriendshipEvent;
import com.sy.piaoliupin.event.GroupEvent;
import com.sy.piaoliupin.event.MessageEvent;
import com.sy.piaoliupin.event.RefreshEvent;
import com.sy.piaoliupin.model.UserInfo;
import com.sy.piaoliupin.presenter.SplashPresenter;
import com.sy.piaoliupin.service.TLSService;
import com.sy.piaoliupin.service.TlsBusiness;
import com.sy.piaoliupin.ui.customview.DialogActivity;
import com.sy.piaoliupin.utils.LogUtil;
import com.sy.piaoliupin.utils.PushUtil;
import com.sy.piaoliupin.utils.SaveUtils;
import com.sy.piaoliupin.utils.TabToast;
import com.sy.piaoliupin.viewfeatures.SplashView;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends Base_Activity implements SplashView, TIMCallBack, TIMUserStatusListener {
    private static final String TAG = "SplashActivity";

    SplashPresenter presenter;
    private int LOGIN_RESULT_CODE = 100;
    private int GOOGLE_PLAY_RESULT_CODE = 200;
    private final int REQUEST_PHONE_PERMISSIONS = 0;


    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,SplashActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clearNotification();

        setContentView(R.layout.activity_splash);

        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.READ_PHONE_STATE);
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionsList.size() == 0) {
                init();
            } else {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_PHONE_PERMISSIONS);
            }
        } else {
            init();
        }

    }

    /**
     * 跳转到主界面
     */
    @Override
    public void navToHome() {
        //登录之前要初始化群和好友关系链缓存
        TIMUserConfig userConfig = new TIMUserConfig();
        userConfig.setUserStatusListener(this);

        //设置刷新监听
        RefreshEvent.getInstance().init(userConfig);
        userConfig = FriendshipEvent.getInstance().init(userConfig);
        userConfig = GroupEvent.getInstance().init(userConfig);
        userConfig = MessageEvent.getInstance().init(userConfig);
        TIMManager.getInstance().setUserConfig(userConfig);
        LoginBusiness.loginIm(UserInfo.getInstance().getId(), UserInfo.getInstance().getUserSig(), this);
    }

    @Override
    public void onForceOffline() {
        DialogActivity.start(this);

    }

    @Override
    public void onUserSigExpired() {
        new NotifyDialog().show(getString(R.string.tls_expire), getSupportFragmentManager(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                            logout();
            }
        });

    }

    /**
     * 跳转到登录界面
     */
    @Override
    public void navToLogin() {

        //判定首次启动
        if (SaveUtils.getBoolean(Save_Key.S_跳过引导)) {
            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
            startActivityForResult(intent, LOGIN_RESULT_CODE);
            finish();
        }else {
            Guide_Activity.start(this);
            finish();
        }
    }

    /**
     * 是否已有用户登录
     */
    @Override
    public boolean isUserLogin() {
        return UserInfo.getInstance().getId() != null && (!TLSService.getInstance().needLogin(UserInfo.getInstance().getId()));
    }

    /**
     * imsdk登录失败后回调
     */
    @Override
    public void onError(int i, String s) {
        LogUtil.e(TAG, "login error : code " + i + " " + s);
        switch (i) {
            case 6208:
                //离线状态下被其他终端踢下线
                NotifyDialog dialog = new NotifyDialog();
                dialog.show(getString(R.string.kick_logout), getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navToHome();
                    }
                });
                break;
            case 6200:
                TabToast.makeText(getString(R.string.login_error_timeout));
                navToLogin();
                break;
            default:
                TabToast.makeText(getString(R.string.login_error));
                navToLogin();
                break;
        }

    }

    /**
     * imsdk登录成功后回调
     */
    @Override
    public void onSuccess() {

        //初始化程序后台后消息推送
        PushUtil.getInstance();
        //初始化消息监听
        MessageEvent.getInstance();
        String deviceMan = android.os.Build.MANUFACTURER;
        //注册小米和华为推送
        if (deviceMan.equals("Xiaomi") && shouldMiInit()) {
            MiPushClient.registerPush(getApplicationContext(), "2882303761517480335", "5411748055335");
        } else if (deviceMan.equals("HUAWEI")) {
            PushManager.requestToken(this);
        }

        //魅族推送只适用于Flyme系统,因此可以先行判断是否为魅族机型，再进行订阅，避免在其他机型上出现兼容性问题
        if (MzSystemUtils.isBrandMeizu(getApplicationContext())) {
            com.meizu.cloud.pushsdk.PushManager.register(this, "112662", "3aaf89f8e13f43d2a4f97a703c6f65b3");
        }

        LogUtil.d(TAG, "imsdk env " + TIMManager.getInstance().getEnv());
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d(TAG, "onActivityResult code:" + requestCode);
        if (LOGIN_RESULT_CODE == requestCode) {
            LogUtil.d(TAG, "login error no " + TLSService.getInstance().getLastErrno());
            if (0 == TLSService.getInstance().getLastErrno()) {
                String id = TLSService.getInstance().getLastUserIdentifier();
                UserInfo.getInstance().setId(id);
                UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));
                navToHome();
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    Toast.makeText(this, getString(R.string.need_permission), Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void init() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        int loglvl = pref.getInt("loglvl", TIMLogLevel.DEBUG.ordinal());
        //初始化IMSDK
        InitBusiness.start(getApplicationContext(), loglvl);
        //初始化TLS

        TlsBusiness.init(getApplicationContext());
        String id = TLSService.getInstance().getLastUserIdentifier();

//        UserInfo.getInstance().setId(id);
//        UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));

        UserInfo.getInstance().setId(SaveUtils.getString(Save_Key.UID));
        UserInfo.getInstance().setUserSig(SaveUtils.getString(Save_Key.S_密码));
        presenter = new SplashPresenter(this);
        presenter.start();
    }

    /**
     * 判断小米推送是否已经初始化
     */
    private boolean shouldMiInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 清楚所有通知栏通知
     */
    private void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        MiPushClient.clearNotification(getApplicationContext());
    }


}
