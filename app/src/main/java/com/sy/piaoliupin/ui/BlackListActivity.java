package com.sy.piaoliupin.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.adapters.ProfileSummaryAdapter;
import com.sy.piaoliupin.model.FriendProfile;
import com.sy.piaoliupin.model.ProfileSummary;
import com.sy.piaoliupin.utils.LogUtil;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO:黑名单
 */

public class BlackListActivity extends Base_Activity {

    private final String TAG = "BlackListActivity";

    ProfileSummaryAdapter adapter;
    List<ProfileSummary> list = new ArrayList<>();
    ListView listView;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, BlackListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);

        setBack(true);
        setTitle("黑名单");

        listView = findViewById(R.id.list);
        adapter = new ProfileSummaryAdapter(this, R.layout.item_profile_summary, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list.get(position).onClick(BlackListActivity.this);
            }
        });
        TIMFriendshipManagerExt.getInstance().getBlackList(new TIMValueCallBack<List<String>>() {
            @Override
            public void onError(int i, String s) {
                LogUtil.e(TAG, "get black list error code : " + i);
            }

            @Override
            public void onSuccess(List<String> ids) {
                TIMFriendshipManagerExt.getInstance().getFriendsProfile(ids, new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override
                    public void onError(int i, String s) {
                        LogUtil.e(TAG, "get profile error code : " + i);
                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                        for (TIMUserProfile item : timUserProfiles) {
                            FriendProfile profile = new FriendProfile(item);
                            list.add(profile);
                        }
                        adapter.notifyDataSetChanged();

                    }
                });

            }
        });
    }
}
