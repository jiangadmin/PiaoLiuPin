package com.sy.piaoliupin.activity.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.dialog.Base_Dialog;

/**
 * 随缘页面
 */
public class BottleFragment extends Base_Fragment implements View.OnClickListener {
    private static final String TAG = "BottleFragment";

    private View view;

    /**
     * 捞瓶子  扔瓶子
     */
    Button get, set;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_bottle, container, false);

        }
        initview();

        setTitle(view, "随缘");

        return view;
    }

    private void initview() {
        get = view.findViewById(R.id.bottle_get);
        set = view.findViewById(R.id.bottle_set);

        get.setOnClickListener(this);
        set.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottle_get:
                Base_Dialog base_dialog = new Base_Dialog(getActivity());
                base_dialog.setTitle("标题");
                base_dialog.setMessage("dasdadasdasdasd");
                base_dialog.setOk("好",null);
                base_dialog.setEsc("否",null);
                break;
            case R.id.bottle_set:
                break;
        }
    }
}
