package com.sy.piaoliupin.activity.welcome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.service.Constants;
import com.sy.piaoliupin.service.TLSService;

/**
 * @author: jiangyao
 * @date: 2018/5/18
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 登录
 */
public class Login_Activity extends Base_Activity  {
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

    }

    private void initview() {

        wechat = findViewById(R.id.login_wechat);
        qq = findViewById(R.id.login_qq);

        tlsService.initQQLoginService(this, qq);
        tlsService.initWXLoginService(this, wechat);
    }


}
