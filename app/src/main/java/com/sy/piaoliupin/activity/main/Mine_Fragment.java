package com.sy.piaoliupin.activity.main;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.mine.Setting_Activity;
import com.sy.piaoliupin.dialog.FriendShip_Dialog;
import com.sy.piaoliupin.entity.Save_Key;
import com.sy.piaoliupin.presenter.FriendshipManagerPresenter;
import com.sy.piaoliupin.ui.BlackListActivity;
import com.sy.piaoliupin.ui.CircleImageView;
import com.sy.piaoliupin.ui.EditActivity;
import com.sy.piaoliupin.ui.ListPickerDialog;
import com.sy.piaoliupin.utils.SaveUtils;
import com.sy.piaoliupin.utils.TabToast;
import com.sy.piaoliupin.viewfeatures.FriendInfoView;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendAllowType;
import com.tencent.imsdk.TIMUserProfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设置页面
 */
public class Mine_Fragment extends Base_Fragment implements FriendInfoView, View.OnClickListener {
    private static final String TAG = "SettingFragment";

    private View view;
    private FriendshipManagerPresenter friendshipManagerPresenter;
    private TextView id;

    private final int REQ_CHANGE_NICK = 1000;
    private Map<String, TIMFriendAllowType> allowTypeContent;

    String[] stringList;

    LinearLayout mine_info;

    TextView nickname;

    ImageView sex;

    Button friendConfirm, blackList, setting;

    CircleImageView head;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mine, container, false);

            setTitle(view, "我的");

            initview();

            initeven();

            friendshipManagerPresenter = new FriendshipManagerPresenter(this);
            friendshipManagerPresenter.getMyProfile();

            allowTypeContent = new HashMap<>();
            allowTypeContent.put(getString(R.string.friend_need_confirm), TIMFriendAllowType.TIM_FRIEND_NEED_CONFIRM);
            allowTypeContent.put(getString(R.string.friend_refuse_all), TIMFriendAllowType.TIM_FRIEND_DENY_ANY);
            allowTypeContent.put(getString(R.string.friend_allow_all), TIMFriendAllowType.TIM_FRIEND_ALLOW_ANY);
            stringList = allowTypeContent.keySet().toArray(new String[allowTypeContent.size()]);

        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(this).load(SaveUtils.getString(Save_Key.S_头像)).into(head);
        id.setText(SaveUtils.getString(Save_Key.UID));
        nickname.setText(SaveUtils.getString(Save_Key.S_昵称));
        sex.setImageResource(SaveUtils.getString(Save_Key.S_性别).equals("男") ? R.drawable.ic_boy : R.drawable.ic_girl);
    }

    /**
     * 初始化控件
     */
    private void initview() {
        head = view.findViewById(R.id.mine_head);
        mine_info = view.findViewById(R.id.mine_info);
        id = view.findViewById(R.id.idtext);

        nickname = view.findViewById(R.id.mine_nickname);
        sex = view.findViewById(R.id.mine_sex);
        friendConfirm = view.findViewById(R.id.mine_friend_Confirm);
        blackList = view.findViewById(R.id.mine_blackList);
        setting = view.findViewById(R.id.mine_setting);

    }

    /**
     * 初始化点击事件
     */
    private void initeven() {
        mine_info.setOnClickListener(this);
        friendConfirm.setOnClickListener(this);
        blackList.setOnClickListener(this);
        setting.setOnClickListener(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CHANGE_NICK) {
            if (resultCode == getActivity().RESULT_OK) {
                setNickName(data.getStringExtra(EditActivity.RETURN_EXTRA));
            }
        }

    }

    private void setNickName(String name) {
        if (name == null) return;
        this.nickname.setText(name);
//        nickName.setContent(name);
    }


    /**
     * 显示用户信息
     *
     * @param users 资料列表
     */
    @Override
    public void showUserInfo(List<TIMUserProfile> users) {
        id.setText(users.get(0).getIdentifier());
        setNickName(users.get(0).getNickName());
        for (String item : allowTypeContent.keySet()) {
            if (allowTypeContent.get(item) == users.get(0).getAllowType()) {
//                friendConfirm.setContent(item);
                break;
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //个人信息
            case R.id.mine_info:
                EditActivity.navToEdit(Mine_Fragment.this, getResources().getString(R.string.setting_nick_name_change), nickname.getText().toString(), REQ_CHANGE_NICK, new EditActivity.EditInterface() {
                    @Override
                    public void onEdit(String text, TIMCallBack callBack) {
                        FriendshipManagerPresenter.setMyNick(text, callBack);
                    }
                }, 20);
                break;

            //好友申请
            case R.id.mine_friend_Confirm:

                new FriendShip_Dialog(getActivity());

                break;

            //黑名单
            case R.id.mine_blackList:
                BlackListActivity.start(getActivity());
                break;

            //设置
            case R.id.mine_setting:
                Setting_Activity.start(getActivity());
                break;
        }
    }
}
