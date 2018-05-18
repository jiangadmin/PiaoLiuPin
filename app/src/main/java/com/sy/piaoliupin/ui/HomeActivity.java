package com.sy.piaoliupin.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.activity.main.BottleFragment;
import com.sy.piaoliupin.activity.main.ContactFragment;
import com.sy.piaoliupin.activity.main.ConversationFragment;
import com.sy.piaoliupin.activity.main.Mine_Fragment;
import com.sy.piaoliupin.utils.TabToast;
import com.sy.piaoliupin.utils.ToolUtils;
import com.tencent.imsdk.TIMManager;
import com.sy.piaoliupin.R;
import com.sy.piaoliupin.event.MessageEvent;
import com.sy.piaoliupin.model.FriendshipInfo;
import com.sy.piaoliupin.model.GroupInfo;
import com.sy.piaoliupin.model.UserInfo;
import com.sy.piaoliupin.service.TlsBusiness;

import java.util.ArrayList;
import java.util.List;

/**
 * Tab页主界面
 */
public class HomeActivity extends Base_Activity {
    private static final String TAG = "HomeActivity";
    private LayoutInflater layoutInflater;
    private FragmentTabHost mTabHost;
    private final Class fragmentArray[] = {ConversationFragment.class,BottleFragment.class, ContactFragment.class, Mine_Fragment.class};
    private int mTitleArray[] = {R.string.home_conversation_tab,R.string.home_bottle_tab, R.string.home_contact_tab, R.string.home_mine_tab};
    private int mImageViewArray[] = {R.drawable.tab_conversation,R.drawable.tab_bottle, R.drawable.tab_contact, R.drawable.tab_setting};
    private String mTextviewArray[] = {"contact","bottle", "conversation", "setting"};
    private ImageView msgUnread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ToolUtils.getMyUUID(this);

        setDarkStatusIcon(false);

        if (requestPermission()) {
            Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
            finish();
            startActivity(intent);
        } else {
            initView();
            TabToast.makeText( getString(TIMManager.getInstance().getEnv() == 0 ? R.string.env_normal : R.string.env_test));
        }

    }

    private void initView() {
        layoutInflater = LayoutInflater.from(this);
        mTabHost = findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.contentPanel);
        int fragmentCount = fragmentArray.length;
        for (int i = 0; i < fragmentCount; ++i) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            mTabHost.getTabWidget().setDividerDrawable(null);

        }

    }

    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.home_tab, null);
        ImageView icon = view.findViewById(R.id.icon);
        icon.setImageResource(mImageViewArray[index]);
        TextView title = view.findViewById(R.id.title);
        title.setText(mTitleArray[index]);
        if (index == 0) {
            msgUnread = view.findViewById(R.id.tabUnread);
        }
        return view;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 设置未读tab显示
     */
    public void setMsgUnread(boolean noUnread) {
        msgUnread.setVisibility(noUnread ? View.GONE : View.VISIBLE);
    }


    private boolean requestPermission() {
        if (afterM()) {
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                    (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)) {
                return true;
            }
        }
        return false;
    }

    private boolean afterM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
    }
}
