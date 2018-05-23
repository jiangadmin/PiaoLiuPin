package com.sy.piaoliupin.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.piaoliupin.dialog.Loading;
import com.sy.piaoliupin.entity.Base_Entity;
import com.sy.piaoliupin.entity.Const;
import com.sy.piaoliupin.utils.HttpUtil;
import com.sy.piaoliupin.utils.LogUtil;
import com.sy.piaoliupin.utils.TabToast;

/**
 * @author: jiangyao
 * @date: 2018/5/23
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 获取个人信息
 */
public class GetUserInfo_Servlet extends AsyncTask<String, Integer, Base_Entity> {
    private static final String TAG = "GetUserInfo_Servlet";

    @Override
    protected Base_Entity doInBackground(String... strings) {
        String res = HttpUtil.doGet(Const.API + "comusers/"+strings[0]);
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
                break;
            default:
                TabToast.makeText(entity.getMessage());
                LogUtil.e(TAG, entity.getMessage());
                break;
        }
    }
}
