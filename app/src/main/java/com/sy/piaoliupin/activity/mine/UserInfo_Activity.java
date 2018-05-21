package com.sy.piaoliupin.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.dialog.Loading;
import com.sy.piaoliupin.entity.Banner_Entity;
import com.sy.piaoliupin.utils.TabToast;
import com.sy.piaoliupin.view.ImageCycleView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 修改个人信息
 */

public class UserInfo_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "UserInfo_Edit_Activity";

    List<Banner_Entity.DBean> beans = new ArrayList<>();

    ImageCycleView imageCycleView;
    ImageButton edit;
    TextView nickName, city, sextv, birthday;

    public static String areacode;

    static String sex = "m";//m：男 f:女

    int jobid;

    public boolean isfrist; //判断是否是第一次

    public static List<String> headurls;
    public String likes;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, UserInfo_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo_edit);

        setTitle("修改个人信息");
        setBack(true);
        setMenu("确定");

        initview();

        initeven();
    }


    protected void initview() {
        imageCycleView = findViewById(R.id.userinfo_edit_circleimageview);
        edit = findViewById(R.id.userinfo_edit_edit);
        nickName = findViewById(R.id.userinfo_nickname);
        city = findViewById(R.id.userinfo_edit_city);
        sextv = findViewById(R.id.userinfo_edit_sex);
        birthday = findViewById(R.id.userinfo_edit_birthday);

        initDatePicker();

    }

    @Override
    protected void onResume() {

        Loading.show(this, "加载中");

        super.onResume();
    }

    protected void initeven() {
        edit.setOnClickListener(this);
        city.setOnClickListener(this);
        sextv.setOnClickListener(this);
        birthday.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userinfo_edit_edit:
//                IMUserInfoImgActivity.start(this, headurls, false);
                break;
            case R.id.userinfo_edit_city:
//                new ChooseCity_Dialog(this, city).show();
                break;
            case R.id.userinfo_edit_sex:
//                new ChooseSex_Dialog(this).show();
                break;

            case R.id.userinfo_edit_birthday:


                break;
        }
    }

    /**
     * 性别选择回传
     *
     * @param sext
     */
    public void setSex(String sext) {
        if (sext.equals("男")) {
            sex = "m";
        }
        if (sext.equals("女")) {
            sex = "f";
        }
        sextv.setText(sext);
    }


    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());


    }


    public void Submit() {

        //昵称
        if (TextUtils.isEmpty(nickName.getText().toString())) {
            TabToast.makeText("请填写昵称");
            return;
        }

        if ("点击选择日期".equals(birthday.getText().toString())) {
            TabToast.makeText("请填写生日");
            return;
        }

        if ("点击选择位置".equals(city.getText().toString())) {
            TabToast.makeText("请设长居地");
            return;
        }


        if (TextUtils.isEmpty(areacode)) {
            TabToast.makeText("请设长居地");
            return;
        }

        Loading.show(this, "保存中");

    }
}
