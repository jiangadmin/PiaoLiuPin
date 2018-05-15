package com.sy.piaoliupin.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sy.piaoliupin.R;

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


}
