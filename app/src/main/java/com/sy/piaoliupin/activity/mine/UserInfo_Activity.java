package com.sy.piaoliupin.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sy.piaoliupin.MyApplication;
import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.dialog.Loading;
import com.sy.piaoliupin.entity.Banner_Entity;
import com.sy.piaoliupin.servlet.Register_Servlet;
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
    TextView nickName, city, sextv,phone;


    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, UserInfo_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo_edit);

        setTitle("个人资料");
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
        phone = findViewById(R.id.userinfo_phone);

        initDatePicker();

    }

    protected void initeven() {
        edit.setOnClickListener(this);
        city.setOnClickListener(this);
        sextv.setOnClickListener(this);


        nickName.setText(MyApplication.userInfo.getNickname());
        city.setText(MyApplication.userInfo.getProvince() + "-" + MyApplication.userInfo.getCity());
        sextv.setText(MyApplication.userInfo.getGender());
        phone.setText(MyApplication.userInfo.getPhone());

        Banner_Entity.DBean dBean = new Banner_Entity.DBean();
        dBean.setPicUrl(MyApplication.userInfo.getFigureurl());
        beans.add(dBean);

        imageCycleView.setBeans(beans, new ImageCycleView.Listener() {
            @Override
            public void displayImage(String imageURL, ImageView imageView) {

            }

            @Override
            public void onImageClick(Banner_Entity.DBean bean, View imageView) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:

                new Register_Servlet().execute();

                break;
            case R.id.userinfo_edit_edit:
//                IMUserInfoImgActivity.start(this, headurls, false);
                break;
            case R.id.userinfo_edit_city:
//                new ChooseCity_Dialog(this, city).show();
                break;
            case R.id.userinfo_edit_sex:
//                new ChooseSex_Dialog(this).show();
                break;

        }
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

        Loading.show(this, "保存中");

    }
}
