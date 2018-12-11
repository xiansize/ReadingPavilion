package com.tcsoft.read.action.interfaces;

import com.tcsoft.read.callback.ActionListener;

/**
 * Created by xiansize on 2017/11/21.
 */
public interface LoginAction {

    void inputCodeLogin(String code,ActionListener<String> listener);

    void scanCodeLogin(String stamp,ActionListener<String> listener);

    void getQrCode(String stamp,ActionListener<String> listener);


    void getToken(ActionListener<String> listener);

    void authOrderCode(String readId,ActionListener<String> listener);

}
