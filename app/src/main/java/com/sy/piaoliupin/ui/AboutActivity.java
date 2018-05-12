package com.sy.piaoliupin.ui;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.qalsdk.QALSDKManager;
import com.sy.piaoliupin.R;

import tencent.tls.platform.TLSHelper;

/**
 * 关于
 */

public class AboutActivity extends FragmentActivity {
    private static final String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        LineControllerView imsdk = findViewById(R.id.imsdk);
        imsdk.setContent(TIMManager.getInstance().getVersion());
        LineControllerView qalsdk = findViewById(R.id.qalsdk);
        qalsdk.setContent(QALSDKManager.getInstance().getSdkVersion());
        LineControllerView tlssdk = findViewById(R.id.tlssdk);
        tlssdk.setContent(TLSHelper.getInstance().getSDKVersion());
        final LineControllerView env = findViewById(R.id.env);
        env.setContent(getString(TIMManager.getInstance().getEnv() == 0 ? R.string.about_env_normal : R.string.about_env_test));
        final String[] envs = new String[]{getString(R.string.about_env_normal), getString(R.string.about_env_test)};
        env.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ListPickerDialog().show(envs, getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        env.setContent(envs[which]);
                        TIMManager.getInstance().setEnv(which);
                        Toast.makeText(AboutActivity.this, getString(R.string.about_set_effect), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        final LineControllerView log = findViewById(R.id.logLevel);
        final TIMLogLevel[] logLevels = TIMLogLevel.values();
        final String[] logNames = new String[logLevels.length];
        for (int i = 0; i < logLevels.length; ++i) {
            logNames[i] = logLevels[i].name();
        }
        log.setContent(TIMManager.getInstance().getSdkConfig().getLogLevel().name());
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ListPickerDialog().show(logNames, getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        log.setContent(logNames[which]);
                        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                        editor.putInt("loglvl", which);
                        editor.apply();
                        Toast.makeText(AboutActivity.this, getString(R.string.about_set_effect), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


}