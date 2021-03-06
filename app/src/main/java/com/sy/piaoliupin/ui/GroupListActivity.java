package com.sy.piaoliupin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.adapters.ProfileSummaryAdapter;
import com.sy.piaoliupin.event.GroupEvent;
import com.sy.piaoliupin.model.GroupInfo;
import com.sy.piaoliupin.model.GroupProfile;
import com.sy.piaoliupin.model.ProfileSummary;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class GroupListActivity extends Base_Activity implements Observer, View.OnClickListener {

    private ProfileSummaryAdapter adapter;
    private ListView listView;
    private String type;
    private List<ProfileSummary> list;
    private final int CREATE_GROUP_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        type = getIntent().getStringExtra("type");
        setTitle();
        listView = findViewById(R.id.list);
        list = GroupInfo.getInstance().getGroupListByType(type);
        adapter = new ProfileSummaryAdapter(this, R.layout.item_profile_summary, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list.get(position).onClick(GroupListActivity.this);
            }
        });

        GroupEvent.getInstance().addObserver(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GroupEvent.getInstance().deleteObserver(this);
    }

    private void setTitle() {
        setBack(true);
        setTitle(GroupInfo.getTypeName(type));
        setMenu(R.drawable.ic_add);

    }


    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable the {@link Observable} object.
     * @param data       the data passed to {@link Observable#notifyObservers(Object)}.
     */
    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof GroupEvent) {
            if (data instanceof GroupEvent.NotifyCmd) {
                GroupEvent.NotifyCmd cmd = (GroupEvent.NotifyCmd) data;
                switch (cmd.type) {
                    case DEL:
                        delGroup((String) cmd.data);
                        break;
                    case ADD:
                        addGroup((TIMGroupCacheInfo) cmd.data);
                        break;
                    case UPDATE:
                        updateGroup((TIMGroupCacheInfo) cmd.data);
                        break;
                }
            }
        }
    }

    private void delGroup(String groupId) {
        Iterator<ProfileSummary> it = list.iterator();
        while (it.hasNext()) {
            ProfileSummary item = it.next();
            if (item.getIdentify().equals(groupId)) {
                it.remove();
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }


    private void addGroup(TIMGroupCacheInfo info) {
        if (info != null && info.getGroupInfo().getGroupType().equals(type)) {
            GroupProfile profile = new GroupProfile(info);
            list.add(profile);
            adapter.notifyDataSetChanged();
        }

    }

    private void updateGroup(TIMGroupCacheInfo info) {
        delGroup(info.getGroupInfo().getGroupId());
        addGroup(info);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                Intent intent = new Intent(GroupListActivity.this, CreateGroupActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
                break;
        }
    }
}
