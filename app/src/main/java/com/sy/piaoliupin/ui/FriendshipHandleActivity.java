package com.sy.piaoliupin.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sy.piaoliupin.activity.Base_Activity;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.sy.piaoliupin.R;
import com.sy.piaoliupin.presenter.FriendshipManagerPresenter;

public class FriendshipHandleActivity extends Base_Activity implements View.OnClickListener {

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendship_handle);

        setTitle("申请详情");
        setBack(true);

        id = getIntent().getStringExtra("id");
        TextView tvName = findViewById(R.id.name);
        tvName.setText(id);
        String word = getIntent().getStringExtra("word");
        TextView tvWord = findViewById(R.id.word);
        tvWord.setText(word);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReject:
                FriendshipManagerPresenter.refuseFriendRequest(id, new TIMValueCallBack<TIMFriendResult>() {
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(FriendshipHandleActivity.this, getString(R.string.operate_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(TIMFriendResult timFriendResult) {
                        Intent intent = new Intent();
                        intent.putExtra("operate", false);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                break;
            case R.id.btnAgree:
                FriendshipManagerPresenter.acceptFriendRequest(id, new TIMValueCallBack<TIMFriendResult>() {
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(FriendshipHandleActivity.this, getString(R.string.operate_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(TIMFriendResult timFriendResult) {
                        Intent intent = new Intent();
                        intent.putExtra("operate", true);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                break;
        }
    }
}
