package com.tcsoft.read.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;

import com.tcsoft.read.action.implement.LoginActionImpl;
import com.tcsoft.read.action.interfaces.LoginAction;
import com.tcsoft.read.callback.ActionListener;
import com.tcsoft.read.utils.Constant;
import com.tcsoft.read.utils.ToastUtil;

public class TokenReceiver extends BroadcastReceiver {

    private static final String TAG = "TokenReceiver";

    private LoginAction loginAction = new LoginActionImpl();

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        loginAction.getToken(new ActionListener<String>() {
            @Override
            public void onSucceed(String s) {
                Log.d(TAG,"更新token成功");
            }

            @Override
            public void onFailed(String message) {
                Log.d(TAG,"更新token失败");
            }
        });

        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
