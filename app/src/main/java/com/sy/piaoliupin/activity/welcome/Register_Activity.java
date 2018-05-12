package com.sy.piaoliupin.activity.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.helper.SmsContentObserver;
import com.sy.piaoliupin.service.TLSService;

/**
 * 注册
 */

public class Register_Activity extends Base_Activity {
    private static final String TAG = "Register_Activity";

    private TLSService tlsService;
    private SmsContentObserver smsContentObserver = null;

    public static void start(Context contexts) {
        Intent intent = new Intent();
        intent.setClass(contexts, Register_Activity.class);
        contexts.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        setTitle("注册");
        setBack(true);
//        ToolBarStyle(0);

        tlsService = TLSService.getInstance();
        tlsService.initSmsRegisterService(this,
                (EditText) findViewById(R.id.selectCountryCode_hostRegister),
                (EditText) findViewById(R.id.phoneNumber_hostRegister),
                (EditText) findViewById(R.id.checkCode_hostRegister),
                (Button) findViewById(R.id.btn_requireCheckCode_hostRegister),
                (Button) findViewById(R.id.btn_hostRegister)
        );


/*        smsContentObserver = new SmsContentObserver(new Handler(),
                this,
                (EditText) findViewById(R.id.checkCode_hostRegister")),
                Constants.SMS_REGISTER_SENDER);
        //注册短信变化监听
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsContentObserver);*/
    }

    protected void onDestroy() {
        super.onDestroy();
        if (smsContentObserver != null) {
            this.getContentResolver().unregisterContentObserver(smsContentObserver);
        }
    }
}
