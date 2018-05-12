package com.sy.piaoliupin.activity.welcome;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.helper.MResource;
import com.sy.piaoliupin.service.TLSService;

/**
 * 用户名注册
 */

public class IndependentRegisterActivity extends Base_Activity {

    public final static String TAG = "IndependentRegisterActivity";
    private TLSService tlsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tencent_tls_ui_activity_independent_register);

        setTitle("用户名注册");
        setBack(true);
        ToolBarStyle(0);

        tlsService = TLSService.getInstance();
        tlsService.initAccountRegisterService(this,
                (EditText) findViewById(MResource.getIdByName(getApplication(), "id", "username")),
                (EditText) findViewById(MResource.getIdByName(getApplication(), "id", "password")),
                (EditText) findViewById(MResource.getIdByName(getApplication(), "id", "repassword")),
                (Button) findViewById(MResource.getIdByName(getApplication(), "id", "btn_register"))
        );

    }
}
