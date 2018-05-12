package com.sy.piaoliupin.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.utils.ToolUtils;

/**
 * 随缘页面
 */
public class BottleFragment extends Fragment {
    private static final String TAG = "BottleFragment";

    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_bottle, container, false);

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
}
