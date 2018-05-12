package com.sy.piaoliupin.ui;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.adapters.ExpandGroupListAdapter;
import com.sy.piaoliupin.model.FriendProfile;
import com.sy.piaoliupin.model.FriendshipInfo;
import com.sy.piaoliupin.model.GroupInfo;
import com.sy.piaoliupin.utils.ToolUtils;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * 联系人界面
 */
public class ContactFragment extends Fragment implements View.OnClickListener, Observer {

    private View view;
    private ExpandGroupListAdapter mGroupListAdapter;
    private ExpandableListView mGroupListView;
    private LinearLayout mNewFriBtn, mPublicGroupBtn, mChatRoomBtn, mPrivateGroupBtn;
    Map<String, List<FriendProfile>> friends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_contact, container, false);
            mGroupListView = (ExpandableListView) view.findViewById(R.id.groupList);
            mNewFriBtn = (LinearLayout) view.findViewById(R.id.btnNewFriend);
            mNewFriBtn.setOnClickListener(this);
            mPublicGroupBtn = (LinearLayout) view.findViewById(R.id.btnPublicGroup);
            mPublicGroupBtn.setOnClickListener(this);
            mChatRoomBtn = (LinearLayout) view.findViewById(R.id.btnChatroom);
            mChatRoomBtn.setOnClickListener(this);
            mPrivateGroupBtn = (LinearLayout) view.findViewById(R.id.btnPrivateGroup);
            mPrivateGroupBtn.setOnClickListener(this);
            TemplateTitle title = (TemplateTitle) view.findViewById(R.id.contact_antionbar);
            title.setMoreImgAction(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMoveDialog();
                }
            });
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
        return view;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        RelativeLayout titlebar = view.findViewById(R.id.title_bar_tob);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) titlebar.getLayoutParams();
        //获取状态栏高度 加上 要设置的标题栏高度 等于 标题栏实际高度
        layoutParams.height = ToolUtils.getStatusHeight() + ToolUtils.dp2px(48);
        titlebar.setLayoutParams(layoutParams);

        TextView tv = view.findViewById(R.id.title);

        tv.setText(title);
    }

    @Override
    public void onResume() {
        super.onResume();
        mGroupListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnNewFriend) {
            Intent intent = new Intent(getActivity(), FriendshipManageMessageActivity.class);
            getActivity().startActivity(intent);
        }
        if (view.getId() == R.id.btnPublicGroup) {
            showGroups(GroupInfo.publicGroup);
        }
        if (view.getId() == R.id.btnChatroom) {
            showGroups(GroupInfo.chatRoom);
        }
        if (view.getId() == R.id.btnPrivateGroup) {
            showGroups(GroupInfo.privateGroup);
        }
    }

    private Dialog inviteDialog;
    private TextView addFriend, managerGroup, addGroup;

    private void showMoveDialog() {
        inviteDialog = new Dialog(getActivity(), R.style.dialog);
        inviteDialog.setContentView(R.layout.contact_more);
        addFriend = (TextView) inviteDialog.findViewById(R.id.add_friend);
        managerGroup = (TextView) inviteDialog.findViewById(R.id.manager_group);
        addGroup = (TextView) inviteDialog.findViewById(R.id.add_group);
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
