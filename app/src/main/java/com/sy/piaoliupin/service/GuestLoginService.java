package com.sy.piaoliupin.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.sy.piaoliupin.helper.Util;

import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSUserInfo;

/**
 * Created by markding on 2015/9/16.
 */
public class GuestLoginService {

    private Context mContext;
    private ImageButton mButton;

    public GuestLoginService(Context context, ImageButton button) {
        this.mContext = context;
        this.mButton = button;

        this.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TLSService.getInstance().TLSGuestLogin(new TLSService.GuestLoginListener() {

                    @Override
                    public void OnGuestLoginSuccess(TLSUserInfo tlsUserInfo) {
                        String thirdappPackageNameSucc = Constants.thirdappPackageNameSucc;
                        String thirdappClassNameSucc = Constants.thirdappClassNameSucc;

                        Intent intent = new Intent();
                        intent.putExtra(Constants.EXTRA_LOGIN_WAY, Constants.GUEST_LOGIN);

                        if (thirdappPackageNameSucc != null && thirdappClassNameSucc != null) {
                            intent.setClassName(thirdappPackageNameSucc, thirdappClassNameSucc);
                            mContext.startActivity(intent);
                        } else {
                            ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
                        }
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void OnGuestLoginFail(TLSErrInfo tlsErrInfo) {
                        Util.notOK(mContext, tlsErrInfo);
                    }

                    @Override
                    public void OnGuestLoginTimeout(TLSErrInfo tlsErrInfo) {
                        Util.notOK(mContext, tlsErrInfo);
                    }

                });
            }
        });
    }
}
