package com.tcsoft.read.api.interfaces;

/**
 * Created by xiansize on 2017/11/21.
 */
public interface LoginApi {

    String inputCodeLogin(String code);

    String scanQrCodeLogin(String stamp);

    String getToken();

    String authOrderCode(String readId);

    String getQrCode(String stamp);


}
