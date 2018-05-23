package com.sy.piaoliupin.activity.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.sy.piaoliupin.MyApplication;
import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.customview.EditTextWithClearButton;
import com.sy.piaoliupin.dialog.Loading;
import com.sy.piaoliupin.helper.Util;
import com.sy.piaoliupin.servlet.CheckSmsCode_Servlet;
import com.sy.piaoliupin.servlet.GetSMSCode_Servlet;
import com.sy.piaoliupin.servlet.Register_Servlet;
import com.sy.piaoliupin.utils.TabToast;

/**
 * @author: jiangyao
 * @date: 2018/5/22
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 绑定手机号
 */
public class BindPhone_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "BindPhone_Activity";

    public static int Intenttype;

    EditTextWithClearButton phone, smscode;

    Button getsms;

    public static void start(Context context, int type) {
        Intent intent = new Intent();
        intent.setClass(context, BindPhone_Activity.class);
        Intenttype = type;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindphone);

        setTitle("绑定手机号");

        setMenu("绑定");

        initview();

        initeven();
    }

    private void initview() {
        phone = findViewById(R.id.bindphone_phone);
        smscode = findViewById(R.id.bindphone_smscode);
        getsms = findViewById(R.id.bindphone_getsms);

    }

    private void initeven() {

        getsms.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String phonenum = phone.getText().toString();
        String code = smscode.getText().toString();

        switch (view.getId()) {
            case R.id.menu:
                if (phonenum.length() != 11) {
                    TabToast.makeText("请输入正确的手机号！");
                    return;
                }

                if (code.length() != 6) {
                    TabToast.makeText("请输入正确的验证码！");
                    return;
                }

                Loading.show(this, "验证中");
                MyApplication.userInfo.setPhone(phonenum);
                new CheckSmsCode_Servlet(this).execute(phonenum, code);

                break;
            case R.id.bindphone_getsms:
                if (phonenum.length() == 11) {
                    Loading.show(this, "请稍后");
                    Util.startTimer(getsms, "获取验证码", "重新获取", 60, 1);
                    new GetSMSCode_Servlet().execute(phonenum);
                } else {
                    TabToast.makeText("请输入正确的手机号！");
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
    }
}
