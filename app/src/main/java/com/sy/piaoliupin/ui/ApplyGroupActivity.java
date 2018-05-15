package com.sy.piaoliupin.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.presenter.GroupManagerPresenter;
import com.sy.piaoliupin.utils.TabToast;
import com.tencent.imsdk.TIMCallBack;

public class ApplyGroupActivity extends Base_Activity implements TIMCallBack {

    private final String TAG = "ApplyGroupActivity";

    private String identify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_group);

        setBack(true);
        setTitle("申请加群");

        identify = getIntent().getStringExtra("identify");
        TextView des = findViewById(R.id.description);
        des.setText("申请加入 " + identify);
        final EditText editText = findViewById(R.id.input);
        TextView btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupManagerPresenter.applyJoinGroup(identify, editText.getText().toString(), ApplyGroupActivity.this);
            }
        });
    }

    @Override
    public void onError(int i, String s) {
        if (i == 10013) {
            //已经是群成员
            TabToast.makeText(getString(R.string.group_member_already));
        }
    }

    @Override
    public void onSuccess() {
        Toast.makeText(this, getResources().getString(R.string.send_success), Toast.LENGTH_SHORT).show();
        finish();
    }
}
