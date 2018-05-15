package com.sy.piaoliupin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.presenter.GroupManagerPresenter;
import com.sy.piaoliupin.utils.TabToast;
import com.tencent.imsdk.TIMValueCallBack;

/**
 * 创建群页面
 */
public class CreateGroupActivity extends Base_Activity {
    TextView mAddMembers;
    EditText mInputView;
    String type;
    private final int CHOOSE_MEM_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategroup);

        setBack(true);
        setTitle("创建群");

        type = getIntent().getStringExtra("type");
        mInputView = findViewById(R.id.input_group_name);
        mAddMembers = findViewById(R.id.btn_add_group_member);
        mAddMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInputView.getText().toString().equals("")) {
                    TabToast.makeText(getString(R.string.create_group_need_name));
                } else {
                    Intent intent = new Intent(CreateGroupActivity.this, ChooseFriendActivity.class);
                    startActivityForResult(intent, CHOOSE_MEM_CODE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CHOOSE_MEM_CODE == requestCode) {
            if (resultCode == RESULT_OK) {
                GroupManagerPresenter.createGroup(mInputView.getText().toString(),
                        type,
                        data.getStringArrayListExtra("select"),
                        new TIMValueCallBack<String>() {
                            @Override
                            public void onError(int i, String s) {
                                if (i == 80001) {
                                    TabToast.makeText(getString(R.string.create_group_fail_because_wording));
                                } else {
                                    TabToast.makeText(getString(R.string.create_group_fail));
                                }
                            }

                            @Override
                            public void onSuccess(String s) {
                                TabToast.makeText(getString(R.string.create_group_succeed));
                                finish();
                            }
                        }
                );
            }
        }
    }
}
