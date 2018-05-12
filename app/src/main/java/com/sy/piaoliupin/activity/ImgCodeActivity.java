package com.sy.piaoliupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.helper.MResource;
import com.sy.piaoliupin.service.AccountLoginService;
import com.sy.piaoliupin.service.Constants;
import com.sy.piaoliupin.service.PhonePwdLoginService;
import com.sy.piaoliupin.service.TLSService;
import com.sy.piaoliupin.utils.LogUtil;

/**
 * 图片验证码
 */

public class ImgCodeActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "ImgCodeActivity";

    private static ImageView imgcodeView;

    private EditText imgcodeEdit;
    private TLSService tlsService;
    private int login_way;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tencent_tls_ui_activity_img_code);

        tlsService = TLSService.getInstance();

        imgcodeEdit = findViewById(R.id.txt_checkcode);
        imgcodeView = findViewById(MResource.getIdByName(getApplication(), "id", "imagecode"));
        imgcodeView.setOnClickListener(this);

        Intent intent = getIntent();
        byte[] picData = intent.getByteArrayExtra(Constants.EXTRA_IMG_CHECKCODE);
        login_way = intent.getIntExtra(Constants.EXTRA_LOGIN_WAY, Constants.NON_LOGIN);

        fillImageview(picData);
        findViewById(MResource.getIdByName(getApplication(), "id", "btn_verify")).setOnClickListener(this);
        findViewById(MResource.getIdByName(getApplication(), "id", "btn_cancel")).setOnClickListener(this);
        findViewById(MResource.getIdByName(getApplication(), "id", "refreshImageCode")).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == MResource.getIdByName(getApplication(), "id", "btn_verify")) {
            String imgcode = imgcodeEdit.getText().toString();
            if (login_way == Constants.PHONEPWD_LOGIN) {
                tlsService.TLSPwdLoginVerifyImgcode(imgcode, PhonePwdLoginService.pwdLoginListener);
            } else if (login_way == Constants.USRPWD_LOGIN) {
                tlsService.TLSPwdLoginVerifyImgcode(imgcode, AccountLoginService.pwdLoginListener);
            }
            finish();
        } else if (v.getId() == MResource.getIdByName(getApplication(), "id", "imagecode")
                || v.getId() == MResource.getIdByName(getApplication(), "id", "refreshImageCode")) { // 刷新验证码
            tlsService.TLSPwdLoginReaskImgcode(AccountLoginService.pwdLoginListener);
        }
        if (v.getId() == MResource.getIdByName(getApplication(), "id", "btn_cancel")) {
            finish();
        }
    }

    public static void fillImageview(byte[] picData) {
        if (picData == null)
            return;
        Bitmap bm = BitmapFactory.decodeByteArray(picData, 0, picData.length);
        LogUtil.e(TAG, "w " + bm.getWidth() + ", h " + bm.getHeight());
        imgcodeView.setImageBitmap(bm);
    }
}
