package com.sy.piaoliupin.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import com.sy.piaoliupin.R;
import com.sy.piaoliupin.activity.Base_Activity;
import com.sy.piaoliupin.utils.TabToast;
import com.tencent.imsdk.TIMCallBack;

/**
 * 修改文本页面
 */
public class EditActivity extends Base_Activity implements TIMCallBack, View.OnClickListener {
    private static final String TAG = "EditActivity";

    private static EditInterface editAction;
    public final static String RETURN_EXTRA = "result";
    private static String defaultString;
    private EditText input;
    private static int lenLimit;

    /**
     * 启动修改文本界面
     *
     * @param context    fragment context
     * @param title      界面标题
     * @param defaultStr 默认文案
     * @param reqCode    请求码，用于识别返回结果
     * @param action     操作回调
     */
    public static void navToEdit(Fragment context, String title, String defaultStr, int reqCode, EditInterface action) {
        Intent intent = new Intent(context.getActivity(), EditActivity.class);
        intent.putExtra("title", title);
        context.startActivityForResult(intent, reqCode);
        defaultString = defaultStr;
        editAction = action;
    }


    /**
     * 启动修改文本界面
     *
     * @param context    activity context
     * @param title      界面标题
     * @param defaultStr 默认文案
     * @param reqCode    请求码，用于识别返回结果
     * @param action     操作回调
     */
    public static void navToEdit(Activity context, String title, String defaultStr, int reqCode, EditInterface action) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra("title", title);
        context.startActivityForResult(intent, reqCode);
        defaultString = defaultStr;
        editAction = action;
    }


    /**
     * 启动修改文本界面
     *
     * @param context    fragment context
     * @param title      界面标题
     * @param defaultStr 默认文案
     * @param reqCode    请求码，用于识别返回结果
     * @param action     操作回调
     * @param limit      输入长度限制
     */
    public static void navToEdit(Fragment context, String title, String defaultStr, int reqCode, EditInterface action, int limit) {
        Intent intent = new Intent(context.getActivity(), EditActivity.class);
        intent.putExtra("title", title);
        context.startActivityForResult(intent, reqCode);
        defaultString = defaultStr;
        editAction = action;
        lenLimit = limit;
    }


    /**
     * 启动修改文本界面
     *
     * @param context    activity context
     * @param title      界面标题
     * @param defaultStr 默认文案
     * @param reqCode    请求码，用于识别返回结果
     * @param action     操作回调
     * @param limit      输入长度限制
     */
    public static void navToEdit(Activity context, String title, String defaultStr, int reqCode, EditInterface action, int limit) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra("title", title);
        context.startActivityForResult(intent, reqCode);
        defaultString = defaultStr;
        editAction = action;
        lenLimit = limit;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        input = findViewById(R.id.editContent);
        if (defaultString != null) {
            input.setText(defaultString);
            input.setSelection(defaultString.length());
        }
        if (lenLimit != 0) {
            input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(lenLimit)});
        }
        setBack(true);
        setTitle(getIntent().getStringExtra("title"));
        setMenu("确定", R.color.white);

//        TemplateTitle title = findViewById(R.id.editTitle);
//        title.setTitleText(getIntent().getStringExtra("title"));
//        title.setMoreTextAction(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        defaultString = null;
        editAction = null;
        lenLimit = 0;
    }

    @Override
    public void onError(int i, String s) {
        TabToast.makeText(getResources().getString(R.string.edit_error));
    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent();
        intent.putExtra(RETURN_EXTRA, input.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                editAction.onEdit(input.getText().toString(), this);
                break;
        }
    }

    public interface EditInterface {
        void onEdit(String text, TIMCallBack callBack);
    }
}
