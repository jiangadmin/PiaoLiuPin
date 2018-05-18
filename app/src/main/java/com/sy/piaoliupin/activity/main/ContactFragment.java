package com.sy.piaoliupin.activity.main;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.adapters.ExpandGroupListAdapter;
import com.sy.piaoliupin.model.FriendProfile;
import com.sy.piaoliupin.model.FriendshipInfo;
import com.sy.piaoliupin.model.GroupInfo;
import com.sy.piaoliupin.ui.FriendshipManageMessageActivity;
import com.sy.piaoliupin.ui.GroupListActivity;
import com.sy.piaoliupin.ui.ManageFriendGroupActivity;
import com.sy.piaoliupin.ui.SearchFriendActivity;
import com.sy.piaoliupin.ui.SearchGroupActivity;
import com.sy.piaoliupin.ui.TemplateTitle;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * 联系人界面
 */
public class ContactFragment extends Base_Fragment implements View.OnClickListener, Observer {

    private View view;
    private ExpandGroupListAdapter mGroupListAdapter;
    private ExpandableListView mGroupListView;
    private LinearLayout mNewFriBtn, mPublicGroupBtn, mChatRoomBtn, mPrivateGroupBtn;
    Map<String, List<FriendProfile>> friends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_contact, container, false);
            mGroupListView = view.findViewById(R.id.groupList);
            mNewFriBtn = view.findViewById(R.id.btnNewFriend);
            mNewFriBtn.setOnClickListener(this);
            mPublicGroupBtn = view.findViewById(R.id.btnPublicGroup);
            mPublicGroupBtn.setOnClickListener(this);
            mChatRoomBtn = view.findViewById(R.id.btnChatroom);
            mChatRoomBtn.setOnClickListener(this);
            mPrivateGroupBtn = view.findViewById(R.id.btnPrivateGroup);
            mPrivateGroupBtn.setOnClickListener(this);

            friends = FriendshipInfo.getInstance().getFriends();
            mGroupListAdapter = new ExpandGroupListAdapter(getActivity(), FriendshipInfo.getInstance().getGroups(), friends);
            mGroupListView.setAdapter(mGroupListAdapter);
            mGroupListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                    friends.get(FriendshipInfo.getInstance().getGroups().get(groupPosition)).get(childPosition).onClick(getActivity());
                    return false;
                }
            });
            FriendshipInfo.getInstance().addObserver(this);
            mGroupListAdapter.notifyDataSetChanged();
        }

        setTitle(view, "联系人");

        setMenu(view, R.drawable.ic_add);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mGroupListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.menu:
                showMoveDialog();
                break;
            case R.id.btnNewFriend:
                FriendshipManageMessageActivity.start(getActivity());
                break;
            case R.id.btnPublicGroup:
                showGroups(GroupInfo.publicGroup);
                break;
            case R.id.btnChatroom:
                showGroups(GroupInfo.chatRoom);
                break;
            case R.id.btnPrivateGroup:
                showGroups(GroupInfo.privateGroup);
                break;
        }

    }

    private Dialog inviteDialog;
    private TextView addFriend, managerGroup, addGroup;

    private void showMoveDialog() {
        inviteDialog = new Dialog(getActivity(), R.style.dialog);
        inviteDialog.setContentView(R.layout.contact_more);
        addFriend = inviteDialog.findViewById(R.id.add_friend);
        managerGroup = inviteDialog.findViewById(R.id.manager_group);
        addGroup = inviteDialog.findViewById(R.id.add_group);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchFriendActivity.class);
                getActivity().startActivity(intent);
                inviteDialog.dismiss();
            }
        });
        managerGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ManageFriendGroupActivity.class);
                getActivity().startActivity(intent);
                inviteDialog.dismiss();
            }
        });
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchGroupActivity.class);
                getActivity().startActivity(intent);
                inviteDialog.dismiss();
            }
        });
        Window window = inviteDialog.getWindow();
        window.setGravity(Gravity.TOP | Gravity.RIGHT);
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setAttributes(lp);
        inviteDialog.show();
    }


    private void showGroups(String type) {
        Intent intent = new Intent(getActivity(), GroupListActivity.class);
        intent.putExtra("type", type);
        getActivity().startActivity(intent);
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
        if (observable instanceof FriendshipInfo) {
            mGroupListAdapter.notifyDataSetChanged();
        }
    }
}
