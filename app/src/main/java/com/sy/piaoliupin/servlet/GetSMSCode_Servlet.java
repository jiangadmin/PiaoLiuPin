package com.sy.piaoliupin.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.piaoliupin.MyApplication;
import com.sy.piaoliupin.dialog.Loading;
import com.sy.piaoliupin.entity.Base_Entity;
import com.sy.piaoliupin.entity.Const;
import com.sy.piaoliupin.utils.HttpUtil;
import com.sy.piaoliupin.utils.LogUtil;
import com.sy.piaoliupin.utils.TabToast;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiangyao
 * @date: 2018/5/22
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 获取短信验证码
 */
public class GetSMSCode_Servlet extends AsyncTask<String, Integer, Base_Entity> {
    private static final String TAG = "GetSMSCode_Servlet";

    String phone;

    @Override
    protected Base_Entity doInBackground(String... strings) {
        Map map = new HashMap();
        phone = strings[0];
        map.put("phone", phone);
        String res = HttpUtil.doPost(Const.API + "codes", map);

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

        if (entity.getStatus()==200){
            MyApplication.userInfo.setPhone(phone);
        }

        LogUtil.e(TAG, entity.getMessage());
        TabToast.makeText(entity.getMessage());
    }
}
