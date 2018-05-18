package com.sy.piaoliupin.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushSettings;
import com.tencent.imsdk.TIMValueCallBack;

public class MessageNotifySettingActivity extends Base_Activity {

    private String TAG = "MessageNotifySettingActivity";
    TIMOfflinePushSettings settings;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,MessageNotifySettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_notify_setting);



        setTitle("消息提醒");
        setBack(true);

        final Uri notifyMusic = Uri.parse("android.resource://com.tencent.qcloud.timchat/" + R.raw.dudulu);
        TIMManager.getInstance().getOfflinePushSettings(new TIMValueCallBack<TIMOfflinePushSettings>() {
            @Override
            public void onError(int i, String s) {
//                Log.e(TAG, "get offline push setting error " + s);
            }

            @Override
            public void onSuccess(TIMOfflinePushSettings timOfflinePushSettings) {
                settings = timOfflinePushSettings;
                LineControllerView messagePush = findViewById(R.id.messagePush);
                messagePush.setSwitch(settings.isEnabled());
                messagePush.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setEnabled(isChecked);
                        TIMManager.getInstance().setOfflinePushSettings(settings);
                    }
                });
                LineControllerView c2cMusic = findViewById(R.id.c2cMusic);
                c2cMusic.setSwitch(settings.getC2cMsgRemindSound() != null);
                c2cMusic.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setC2cMsgRemindSound(isChecked ? notifyMusic : null);
                        TIMManager.getInstance().setOfflinePushSettings(settings);
                    }
                });
                LineControllerView groupMusic = findViewById(R.id.groupMusic);
                groupMusic.setSwitch(settings.getGroupMsgRemindSound() != null);
                groupMusic.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setGroupMsgRemindSound(isChecked ? notifyMusic : null);
                        TIMManager.getInstance().setOfflinePushSettings(settings);
                    }
                });
            }
        });

    }
}
