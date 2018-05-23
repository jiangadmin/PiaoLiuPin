package com.sy.piaoliupin.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.piaoliupin.MyApplication;
import com.sy.piaoliupin.activity.welcome.BindPhone_Activity;
import com.sy.piaoliupin.activity.welcome.Login_Activity;
import com.sy.piaoliupin.dialog.Loading;
import com.sy.piaoliupin.entity.Base_Entity;
import com.sy.piaoliupin.entity.Const;
import com.sy.piaoliupin.ui.HomeActivity;
import com.sy.piaoliupin.utils.HttpUtil;
import com.sy.piaoliupin.utils.LogUtil;
import com.sy.piaoliupin.utils.TabToast;
import com.sy.piaoliupin.utils.ToolUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiangyao
 * @date: 2018/5/22
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 注册
 */
public class Register_Servlet extends AsyncTask<String, Integer, Base_Entity> {
    private static final String TAG = "Register_Servlet";

    @Override
    protected Base_Entity doInBackground(String... strings) {
        Map map = new HashMap();
        map.put("nikename", MyApplication.userInfo.getNickname());
        map.put("type", "1");
//        map.put("sign", "1");
        map.put("sex", (MyApplication.userInfo.getGender().equals("男") ? "1" : "2"));
        map.put("phone", MyApplication.userInfo.getPhone());
        map.put("avatar", MyApplication.userInfo.getFigureurl());
        map.put("sign", "这个人很懒，什么都没有留下");
        map.put("openid", MyApplication.userInfo.getOpenid());
        map.put("channel", String.valueOf(MyApplication.userInfo.getLogintype()));
        map.put("device_id", ToolUtils.getMyUUID());

        String res = HttpUtil.doPost(Const.API + "tokens", map);
        Base_Entity entity;

        if (TextUtils.isEmpty(res)) {
            entity = new Base_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败");
        } else {
            try {
                entity = new Gson().fromJson(res, Base_Entity.class);
            } catch (Exception e) {
                entity = new Base_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败");
            }
        }
        return entity;
    }

    @Override
    protected void onPostExecute(Base_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();

        switch (entity.getStatus()) {
            case 200:
                HomeActivity.start(MyApplication.currentActivity());

                MyApplication.finishActivity(Login_Activity.class);
                MyApplication.finishActivity(BindPhone_Activity.class);

                break;
            default:
                TabToast.makeText(entity.getMessage());
                LogUtil.e(TAG, entity.getMessage());
                break;
        }
    }
}
