package com.sy.piaoliupin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.adapters.ExpandGroupListAdapter;
import com.sy.piaoliupin.model.FriendProfile;
import com.sy.piaoliupin.model.FriendshipInfo;
import com.sy.piaoliupin.utils.TabToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO: 选择联系人
 */

public class ChooseFriendActivity extends Base_Activity implements View.OnClickListener {

    private ExpandGroupListAdapter mGroupListAdapter;
    private ExpandableListView mGroupListView;
    private List<FriendProfile> selectList = new ArrayList<>();
    private List<String> mAlreadySelect = new ArrayList<>();
    private List<FriendProfile> alreadySelectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend);

        setBack(true);
        setTitle("选择联系人");
        setMenu("确定");


        List<String> selected = getIntent().getStringArrayListExtra("selected");
        if (selected != null) {
            mAlreadySelect.addAll(selected);
        }


        final Map<String, List<FriendProfile>> friends = FriendshipInfo.getInstance().getFriends();
        for (String id : mAlreadySelect) {
            for (String key : friends.keySet()) {
                for (FriendProfile profile : friends.get(key)) {
                    if (id.equals(profile.getIdentify())) {
                        profile.setIsSelected(true);
                        alreadySelectList.add(profile);
                    }
                }
            }
        }
        mGroupListView = findViewById(R.id.groupList);
        mGroupListAdapter = new ExpandGroupListAdapter(this, FriendshipInfo.getInstance().getGroups(), friends, true);
        mGroupListView.setAdapter(mGroupListAdapter);
        mGroupListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                FriendProfile profile = friends.get(FriendshipInfo.getInstance().getGroups().get(groupPosition)).get(childPosition);
                if (alreadySelectList.contains(profile)) return false;
                onSelect(profile);
                mGroupListAdapter.notifyDataSetChanged();
                return false;
            }
        });
        mGroupListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        for (FriendProfile item : selectList) {
            item.setIsSelected(false);
        }
        for (FriendProfile item : alreadySelectList) {
            item.setIsSelected(false);
        }
    }

    private void onSelect(FriendProfile profile) {
        if (!profile.isSelected()) {
            selectList.add(profile);
        } else {
            selectList.remove(profile);
        }
        profile.setIsSelected(!profile.isSelected());
    }

    private ArrayList<String> getSelectIds() {
        ArrayList<String> result = new ArrayList<>();
        for (FriendProfile item : selectList) {
            result.add(item.getIdentify());
        }
        return result;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:

                if (selectList.size() == 0) {
                    TabToast.makeText(getString(R.string.choose_need_one));
                    return;
                }
                Intent intent = new Intent();
                intent.putStringArrayListExtra("select", getSelectIds());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
