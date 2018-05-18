package com.sy.piaoliupin.servlet;

import android.os.AsyncTask;

import com.sy.piaoliupin.entity.Const;
import com.sy.piaoliupin.utils.HttpUtil;
import com.sy.piaoliupin.utils.LogUtil;

/**
 * @author: jiangyao
 * @date: 2018/5/18
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 公告
 */
public class GetMessage_Servlet extends AsyncTask<String, Integer, String> {
    private static final String TAG = "GetMessage_Servlet";

    /**
     * 公告 3 设置页面 4 亲密度 5 聊天 6 官方公告 7 投诉须知 8 奖励
     * @param strings
     * @return
     */
    @Override
    protected String doInBackground(String... strings) {
        String res = HttpUtil.doGet(Const.URl + "bulletins/" + strings[0]);

        return res;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        LogUtil.e(TAG, s);
    }
}
