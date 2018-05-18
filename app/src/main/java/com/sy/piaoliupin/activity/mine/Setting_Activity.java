package com.sy.piaoliupin.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.business.LoginBusiness;
import com.sy.piaoliupin.event.MessageEvent;
import com.sy.piaoliupin.model.FriendshipInfo;
import com.sy.piaoliupin.model.GroupInfo;
import com.sy.piaoliupin.model.UserInfo;
import com.sy.piaoliupin.service.TlsBusiness;
import com.sy.piaoliupin.ui.AboutActivity;
import com.sy.piaoliupin.ui.HomeActivity;
import com.sy.piaoliupin.ui.MessageNotifySettingActivity;
import com.sy.piaoliupin.utils.TabToast;
import com.tencent.imsdk.TIMCallBack;

/**
 * @author: jiangyao
 * @date: 2018/5/17
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 设置
 */
public class Setting_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "Setting_Activity";

    Button message, clean, help, about, out;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,Setting_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        setTitle("设置");
        setBack(true);

        initview();
        initeven();
    }

    private void initview() {
        message = findViewById(R.id.setting_message);
        clean = findViewById(R.id.setting_clean);
        help = findViewById(R.id.setting_help);
        about = findViewById(R.id.setting_about);
        out = findViewById(R.id.setting_out);

    }

    private void initeven() {

        message.setOnClickListener(this);
        clean.setOnClickListener(this);
        help.setOnClickListener(this);
        about.setOnClickListener(this);
        out.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //消息提醒
            case R.id.setting_message:
                MessageNotifySettingActivity.start(this);
                break;
            //清除缓存
            case R.id.setting_clean:
                break;
            //帮助与反馈
            case R.id.setting_help:
                break;
            //关于
            case R.id.setting_about:
                AboutActivity.start(this);
                break;
            //退出
            case R.id.setting_out:
                LoginBusiness.logout(new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        TabToast.makeText(getResources().getString(R.string.setting_logout_fail));
                    }

                    @Override
                    public void onSuccess() {
                        TlsBusiness.logout(UserInfo.getInstance().getId());
                        UserInfo.getInstance().setId(null);
                        MessageEvent.getInstance().clear();
                        FriendshipInfo.getInstance().clear();
                        GroupInfo.getInstance().clear();
                    }
                });
                break;
        }
    }
}
