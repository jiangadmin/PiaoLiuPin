package com.sy.piaoliupin.activity.welcome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.helper.MResource;
import com.sy.piaoliupin.helper.Util;
import com.sy.piaoliupin.service.Constants;
import com.sy.piaoliupin.service.TLSService;
import com.sy.piaoliupin.ui.HomeActivity;

import java.util.Timer;
import java.util.TimerTask;

import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSPwdRegListener;
import tencent.tls.platform.TLSPwdResetListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * TODO:登录 手机号密码登录
 */

public class LoginForPWD_Activity extends Base_Activity implements View.OnClickListener, TLSPwdLoginListener {
    public final static String TAG = "PhonePwdLoginActivity";

    private TLSService tlsService;
    private int login_way = Constants.PHONEPWD_LOGIN | Constants.QQ_LOGIN | Constants.WX_LOGIN;
    //    private int login_way = Constants.PHONEPWD_LOGIN;
    private String countryCode;
    private String phoneNumber;
    final static int SMS_REG_REQUEST = 10004;
    final static int SMS_RESET_REQUEST = 10005;

    EditText phone, password;

    TextView register, reset;

    Button login;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginForPWD_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tencent_tls_ui_activity_phone_pwd_login);

        Intent intent = getIntent();
        if (Constants.thirdappPackageNameSucc == null)
            Constants.thirdappPackageNameSucc = intent.getStringExtra(Constants.EXTRA_THIRDAPP_PACKAGE_NAME_SUCC);
        if (Constants.thirdappClassNameSucc == null)
            Constants.thirdappClassNameSucc = intent.getStringExtra(Constants.EXTRA_THIRDAPP_CLASS_NAME_SUCC);
        if (Constants.thirdappPackageNameFail == null)
            Constants.thirdappPackageNameFail = intent.getStringExtra(Constants.EXTRA_THIRDAPP_PACKAGE_NAME_FAIL);
        if (Constants.thirdappClassNameFail == null)
            Constants.thirdappClassNameFail = intent.getStringExtra(Constants.EXTRA_THIRDAPP_CLASS_NAME_FAIL);

        tlsService = TLSService.getInstance();

        if ((login_way & Constants.PHONEPWD_LOGIN) != 0) { // 手机号密码登录
//            initPhonePwdService();
        }

        SharedPreferences settings = getSharedPreferences(Constants.TLS_SETTING, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(Constants.SETTING_LOGIN_WAY, Constants.PHONEPWD_LOGIN);
        editor.commit();

        initview();

    }

    private void initview() {
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        register = findViewById(R.id.registerNewUser);
        reset = findViewById(R.id.resetPassword);
        login = findViewById(R.id.btn_login);

        reset.setOnClickListener(this);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //注册
            case R.id.registerNewUser:
                startActivityForResult(new Intent(this, Register_Activity.class), SMS_REG_REQUEST);
                break;
            //重置密码
            case R.id.resetPassword:
                startActivityForResult(new Intent(this, ResetPwd_Activity.class), SMS_RESET_REQUEST);
                break;
            //登录
            case R.id.btn_login:

                startActivity(new Intent(this, HomeActivity.class));
                finish();
//                Login();
                break;
        }
    }

    /**
     * 登录
     */
    public void Login() {
        String xphone = phone.getText().toString();
        String xpassword = password.getText().toString();

        if (TextUtils.isEmpty(xphone)) {

        }


        tlsService.TLSPwdLogin("86-" + xphone, xpassword, this);

    }

    private void initPhonePwdService() {


        tlsService.initPhonePwdLoginService(this,
                (EditText) findViewById(R.id.selectCountryCode),
                (EditText) findViewById(R.id.phone),
                (EditText) findViewById(R.id.password),
                (Button) findViewById(R.id.btn_login)
        );

    }

    //应用调用Andriod_SDK接口时，使能成功接收到回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == SMS_REG_REQUEST) {
            if (RESULT_OK == resultCode) {
                setResult(RESULT_OK, data);
                finish();
            }

        } else if (requestCode == SMS_RESET_REQUEST) {
            setResult(RESULT_OK, data);
//            finish();
        } else {

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        if (intent == null) return;

        // 判断是否是从微信登录界面返回的
        int wx_login = intent.getIntExtra(Constants.EXTRA_WX_LOGIN, Constants.WX_LOGIN_NON);
        if (wx_login != Constants.WX_LOGIN_NON) {
            if (wx_login == Constants.WX_LOGIN_SUCCESS) {
                Intent data = new Intent();
                data.putExtra(Constants.EXTRA_LOGIN_WAY, Constants.WX_LOGIN);
                data.putExtra(Constants.EXTRA_WX_LOGIN, Constants.WX_LOGIN_SUCCESS);
                data.putExtra(Constants.EXTRA_WX_OPENID, intent.getStringExtra(Constants.EXTRA_WX_OPENID));
                data.putExtra(Constants.EXTRA_WX_ACCESS_TOKEN, intent.getStringExtra(Constants.EXTRA_WX_ACCESS_TOKEN));
                if (Constants.thirdappPackageNameSucc != null && Constants.thirdappClassNameSucc != null) {
                    data.setClassName(Constants.thirdappPackageNameSucc, Constants.thirdappClassNameSucc);
                    startActivity(data);
                } else {
                    setResult(RESULT_OK, data);
                }
                finish();
            }
            return;
        }

        // 判断是从注册界面还是重置密码界面返回
        int where = intent.getIntExtra(Constants.EXTRA_PHONEPWD_REG_RST, Constants.PHONEPWD_NON);
        countryCode = intent.getStringExtra(Constants.COUNTRY_CODE);
        phoneNumber = intent.getStringExtra(Constants.PHONE_NUMBER);
        if (where == Constants.PHONEPWD_REGISTER) { // 从注册界面返回

            if (countryCode != null && phoneNumber != null) {
                setPassword(1);      // 弹出填写密码的对话框
            }

            return;

        } else if (where == Constants.PHONEPWD_RESET) { // 从重置密码界面返回

            if (countryCode != null && phoneNumber != null) {
                setPassword(2);      // 弹出填写密码的对话框
            }

            return;
        }
    }

    public void setPassword(final int type) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(MResource.getIdByName(getApplication(), "layout", "tencent_tls_ui_dialog"), null);

        final EditText editText = view.findViewById(MResource.getIdByName(getApplication(), "id", "password"));
        Button btn_confirm = view.findViewById(MResource.getIdByName(getApplication(), "id", "btn_confirm"));
        Button btn_cancel = view.findViewById(MResource.getIdByName(getApplication(), "id", "btn_cancel"));

        final AlertDialog dialog = new AlertDialog.Builder(this).setView(view).setCancelable(false).create();

        dialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String regPassword = editText.getText().toString();
                if (regPassword.length() == 0) {
                    Util.showToast(LoginForPWD_Activity.this, "密码不能为空");
                    return;
                }

                if (type == 1) { // 设置密码
                    tlsService.TLSPwdRegCommit(regPassword, new TLSPwdRegListener() {
                        @Override
                        public void OnPwdRegAskCodeSuccess(int reaskDuration, int expireDuration) {
                        }

                        @Override
                        public void OnPwdRegReaskCodeSuccess(int reaskDuration, int expireDuration) {
                        }

                        @Override
                        public void OnPwdRegVerifyCodeSuccess() {
                        }

                        @Override
                        public void OnPwdRegCommitSuccess(TLSUserInfo userInfo) {
                            Util.showToast(LoginForPWD_Activity.this, "注册成功");
                            ((EditText) findViewById(MResource.getIdByName(getApplication(), "id", "selectCountryCode"))).setText(countryCode);
                            ((EditText) findViewById(MResource.getIdByName(getApplication(), "id", "phone"))).setText(phoneNumber);
                            ((EditText) findViewById(MResource.getIdByName(getApplication(), "id", "password"))).setText(regPassword);

                            findViewById(MResource.getIdByName(getApplication(), "id", "btn_login")).performClick();
                        }

                        @Override
                        public void OnPwdRegFail(TLSErrInfo errInfo) {
                            Util.notOK(LoginForPWD_Activity.this, errInfo);
                        }

                        @Override
                        public void OnPwdRegTimeout(TLSErrInfo errInfo) {
                            Util.notOK(LoginForPWD_Activity.this, errInfo);
                        }
                    });
                }

                if (type == 2) { // 重置密码
                    tlsService.TLSPwdResetCommit(regPassword, new TLSPwdResetListener() {
                        @Override
                        public void OnPwdResetAskCodeSuccess(int reaskDuration, int expireDuration) {
                        }

                        @Override
                        public void OnPwdResetReaskCodeSuccess(int reaskDuration, int expireDuration) {
                        }

                        @Override
                        public void OnPwdResetVerifyCodeSuccess() {
                        }

                        @Override
                        public void OnPwdResetCommitSuccess(TLSUserInfo userInfo) {
                            Util.showToast(LoginForPWD_Activity.this, "重置密码成功");
                            ((EditText) findViewById(MResource.getIdByName(getApplication(), "id", "selectCountryCode"))).setText(countryCode);
                            ((EditText) findViewById(MResource.getIdByName(getApplication(), "id", "phone"))).setText(phoneNumber);
                            ((EditText) findViewById(MResource.getIdByName(getApplication(), "id", "password"))).setText(regPassword);

                            findViewById(MResource.getIdByName(getApplication(), "id", "btn_login")).performClick();
                        }

                        @Override
                        public void OnPwdResetFail(TLSErrInfo errInfo) {
                            Util.notOK(LoginForPWD_Activity.this, errInfo);
                        }

                        @Override
                        public void OnPwdResetTimeout(TLSErrInfo errInfo) {
                            Util.notOK(LoginForPWD_Activity.this, errInfo);
                        }
                    });
                }

                dialog.dismiss();
            }
        });

        showSoftInput(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
    }

    private static void showSoftInput(final Context ctx) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        }, 200);
    }


    @Override
    public void OnPwdLoginSuccess(TLSUserInfo tlsUserInfo) {

    }

    @Override
    public void OnPwdLoginReaskImgcodeSuccess(byte[] bytes) {

    }

    @Override
    public void OnPwdLoginNeedImgcode(byte[] bytes, TLSErrInfo tlsErrInfo) {

    }

    @Override
    public void OnPwdLoginFail(TLSErrInfo tlsErrInfo) {

    }

    @Override
    public void OnPwdLoginTimeout(TLSErrInfo tlsErrInfo) {

    }
}
