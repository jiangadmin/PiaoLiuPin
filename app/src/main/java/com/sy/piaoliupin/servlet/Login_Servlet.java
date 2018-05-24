package com.sy.piaoliupin.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.piaoliupin.MyApplication;
import com.sy.piaoliupin.activity.welcome.BindPhone_Activity;
import com.sy.piaoliupin.dialog.Loading;
import com.sy.piaoliupin.entity.Const;
import com.sy.piaoliupin.entity.Login_Entity;
import com.sy.piaoliupin.entity.Save_Key;
import com.sy.piaoliupin.service.AccountLoginService;
import com.sy.piaoliupin.service.TLSService;
import com.sy.piaoliupin.ui.HomeActivity;
import com.sy.piaoliupin.utils.HttpUtil;
import com.sy.piaoliupin.utils.LogUtil;
import com.sy.piaoliupin.utils.SaveUtils;
import com.sy.piaoliupin.utils.TabToast;
import com.sy.piaoliupin.utils.ToolUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiangyao
 * @date: 2018/5/9
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 登录
 */

public class Login_Servlet extends AsyncTask<String, Integer, Login_Entity> {
    private static final String TAG = "Login_Servlet";

    Activity activity;

    public Login_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Login_Entity doInBackground(String... strings) {

        Map map = new HashMap();
        map.put("channel", strings[0]);
        map.put("openid", strings[1]);
        map.put("device_id", ToolUtils.getMyUUID());

        String res = HttpUtil.doPut(Const.API + "tokens", map);

        LogUtil.e(TAG, res);
        Login_Entity entity;

        if (TextUtils.isEmpty(res)) {
            entity = new Login_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败");
        } else {
            try {
                entity = new Gson().fromJson(res, Login_Entity.class);
            } catch (Exception e) {
                entity = new Login_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败");
            }
        }
        return entity;
    }

    @Override
    protected void onPostExecute(Login_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();

        switch (entity.getStatus()) {
            case 200:

                SaveUtils.setString(Save_Key.UID, entity.getUid());
                SaveUtils.setString(Save_Key.S_校验, entity.getAccess_token());
                SaveUtils.setString(Save_Key.S_密码, entity.getUsersig());

                //腾讯IM用户名密码登录
                TIMManager.getInstance().login( entity.getUid(), entity.getUsersig(), new TIMCallBack() {

                    @Override
                    public void onError(int i, String s) {
                        LogUtil.e(TAG, s);
                    }

                    @Override
                    public void onSuccess() {
                        SaveUtils.setBoolean(Save_Key.S_登录,true);
                        HomeActivity.start(activity);
                        MyApplication.finishActivity(activity);
                    }
                });

                break;
            //未注册
            case 202:
                BindPhone_Activity.start(activity, 1);
                break;
            default:
                TabToast.makeText(entity.getMessage());
                LogUtil.e(TAG, entity.getMessage());
                break;
        }
    }
}
