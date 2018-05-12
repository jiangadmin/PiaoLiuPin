package com.sy.piaoliupin.activity.welcome;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.helper.SmsContentObserver;
import com.sy.piaoliupin.service.TLSService;

public class ResetPwd_Activity extends Base_Activity {

    private final static String TAG = "ResetPhonePwdActivity";

    private TLSService tlsService;
    private SmsContentObserver smsContentObserver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tencent_tls_ui_activity_reset_phone_pwd);

        setBack(true);

        tlsService = TLSService.getInstance();
        tlsService.initResetPhonePwdService(this,
                (EditText) findViewById(R.id.selectCountryCode),
                (EditText) findViewById(R.id.phone),
                (EditText) findViewById(R.id.txt_checkcode),
                (Button) findViewById(R.id.btn_requirecheckcode),
                (Button) findViewById(R.id.btn_verify)
        );

/*        smsContentObserver = new SmsContentObserver(new Handler(),
                this,
                (EditText) findViewById(MResource.getIdByName(getApplication(), "id", "txt_checkcode")),
                Constants.PHONEPWD_RESET_SENDER);

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
