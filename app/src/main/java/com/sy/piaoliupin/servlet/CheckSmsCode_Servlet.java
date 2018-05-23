package com.sy.piaoliupin.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.piaoliupin.MyApplication;
import com.sy.piaoliupin.activity.mine.UserInfo_Activity;
import com.sy.piaoliupin.activity.welcome.Login_Activity;
import com.sy.piaoliupin.dialog.Loading;
import com.sy.piaoliupin.entity.Base_Entity;
import com.sy.piaoliupin.entity.Const;
import com.sy.piaoliupin.utils.HttpUtil;
import com.sy.piaoliupin.utils.LogUtil;
import com.sy.piaoliupin.utils.TabToast;

/**
 * @author: jiangyao
 * @date: 2018/5/22
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 校验短信验证码
 */
public class CheckSmsCode_Servlet extends AsyncTask<String, Integer, Base_Entity> {
    private static final String TAG = "CheckSmsCode_Servlet";

    Activity activity;

    String phone;

    public CheckSmsCode_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Base_Entity doInBackground(String... strings) {
        phone = strings[0];
        String res = HttpUtil.doGet(Const.API + "codes/" + phone + "/" + strings[1]);
        LogUtil.e(TAG, res);
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
                Loading.show(activity,"请稍后");
                MyApplication.userInfo.setPhone(phone);
                //注册操作
                new Register_Servlet().execute();

                break;
            default:
                TabToast.makeText(entity.getMessage());
                break;
        }
    }
}
