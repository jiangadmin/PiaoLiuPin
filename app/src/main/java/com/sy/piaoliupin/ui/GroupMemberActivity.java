package com.sy.piaoliupin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.adapters.ProfileSummaryAdapter;
import com.sy.piaoliupin.model.GroupInfo;
import com.sy.piaoliupin.model.GroupMemberProfile;
import com.sy.piaoliupin.model.ProfileSummary;
import com.sy.piaoliupin.presenter.GroupManagerPresenter;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.tencent.imsdk.ext.group.TIMGroupMemberResult;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberActivity extends Base_Activity implements View.OnClickListener, TIMValueCallBack<List<TIMGroupMemberInfo>> {

    ProfileSummaryAdapter adapter;
    List<ProfileSummary> list = new ArrayList<>();
    ListView listView;
    String groupId, type;
    private final int MEM_REQ = 100;
    private final int CHOOSE_MEM_CODE = 200;
    private int memIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);

        setBack(true);
        setTitle("群成员");

        groupId = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        listView = findViewById(R.id.list);
        adapter = new ProfileSummaryAdapter(this, R.layout.item_profile_summary, list);
        listView.setAdapter(adapter);
        TIMGroupManagerExt.getInstance().getGroupMembers(groupId, this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                memIndex = position;
                Intent intent = new Intent(GroupMemberActivity.this, GroupMemberProfileActivity.class);
                GroupMemberProfile profile = (GroupMemberProfile) list.get(position);
                intent.putExtra("data", profile);
                intent.putExtra("groupId", groupId);
                intent.putExtra("type", type);
                startActivityForResult(intent, MEM_REQ);
            }
        });

        if (type.equals(GroupInfo.privateGroup)) {
            setMenu(R.drawable.ic_add);
        }
    }

    @Override
    public void onError(int i, String s) {

    }

    @Override
    public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
        list.clear();
        if (timGroupMemberInfos == null) return;
        for (TIMGroupMemberInfo item : timGroupMemberInfos) {
            list.add(new GroupMemberProfile(item));
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (MEM_REQ == requestCode) {
            if (resultCode == RESULT_OK) {
                boolean isKick = data.getBooleanExtra("isKick", false);
                if (isKick) {
                    list.remove(memIndex);
                    adapter.notifyDataSetChanged();
                } else {
                    GroupMemberProfile profile = (GroupMemberProfile) data.getSerializableExtra("data");
                    if (memIndex < list.size() && list.get(memIndex).getIdentify().equals(profile.getIdentify())) {
                        GroupMemberProfile mMemberProfile = (GroupMemberProfile) list.get(memIndex);
                        mMemberProfile.setRoleType(profile.getRole());
                        mMemberProfile.setQuietTime(profile.getQuietTime());
                        mMemberProfile.setName(profile.getNameCard());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        } else if (CHOOSE_MEM_CODE == requestCode) {
            if (resultCode == RESULT_OK) {
                GroupManagerPresenter.inviteGroup(groupId, data.getStringArrayListExtra("select"),
                        new TIMValueCallBack<List<TIMGroupMemberResult>>() {
                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(GroupMemberActivity.this, getString(R.string.chat_setting_invite_error), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(List<TIMGroupMemberResult> timGroupMemberResults) {
                                TIMGroupManagerExt.getInstance().getGroupMembers(groupId, GroupMemberActivity.this);
                            }
                        });

            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                Intent intent = new Intent(this, ChooseFriendActivity.class);
                ArrayList<String> selected = new ArrayList<>();
                for (ProfileSummary profile : list) {
                    selected.add(profile.getIdentify());
                }
                intent.putStringArrayListExtra("selected", selected);
                startActivityForResult(intent, CHOOSE_MEM_CODE);
                break;
        }
    }
}
