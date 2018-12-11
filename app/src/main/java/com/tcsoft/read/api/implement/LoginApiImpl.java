package com.tcsoft.read.api.implement;

import com.tcsoft.read.action.interfaces.ContentAction;
import com.tcsoft.read.api.interfaces.LoginApi;
import com.tcsoft.read.netword.NetwordHelper;
import com.tcsoft.read.utils.Constant;
import com.tcsoft.read.utils.MD5Util;
import com.tcsoft.read.utils.TimeUtil;
import com.tcsoft.read.utils.URLEscape2;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiansize on 2017/11/21.
 */
public class LoginApiImpl implements LoginApi {




    @Override
    public String inputCodeLogin(String code) {
        Map<String,String> map = new HashMap<>();
        map.put("token",Constant.URL_TOKEN);
        map.put("number", MD5Util.getMD5Security(code));
        map.put("activityId",Constant.ACTIVITY_ID);
        return NetwordHelper.sendDataByPost(map, Constant.URL_READING+"/webserver/reader/order");
    }


    @Override
    public String scanQrCodeLogin(String stamp) {
//        Map<String,String> map = new HashMap<>();
//        map.put("serialNo",Constant.serialNo);
//        map.put("globalCode",Constant.c);
//        return NetwordHelper.sendDataByPost(map,Constant.URL_GET_SERIAL);

        return NetwordHelper.getDataByGet(Constant.URL_READING+"/webserver/contestant/exit?token=" +  Constant.URL_TOKEN + "&time=" + stamp);


    }




    @Override
    public String getToken() {
        Map<String,String> map = new HashMap<>();
        map.put("machineNO",Constant.DEVICE_ID);
        map.put("pswd",Constant.DEVICE_PASSWORD);
        return NetwordHelper.sendDataByPost(map,Constant.URL_READING+"/webserver/machine/login");
    }

    @Override
    public String authOrderCode(String readId) {
        return NetwordHelper.getDataByGet(Constant.URL_READING+"/webserver/reader/exis?token=" +  Constant.URL_TOKEN + "&rdId=" + readId+"&activityId="+Constant.ACTIVITY_ID);

    }



    @Override
    public String getQrCode(String stamp) {
//        Map<String,String> map = new HashMap<>();
//        map.put("token",Constant.URL_TOKEN);
//        map.put("libraryToken",Constant.LIB_TOKEN);
//        map.put("libraryId",Constant.LIB_CODE);
////        map.put("activityId",Constant.ACTIVITY_ID);
//        map.put("ip",Constant.IP_ADDRESS);
//        return NetwordHelper.sendDataByPost(map,Constant.URL_READING+"/contestant/login");
        //return NetwordHelper.getDataByGet(Constant.URL_READING+"/app/contestant/"+Constant.LIB_CODE+"/login?token=" +  Constant.URL_TOKEN + "&time=" + System.currentTimeMillis()+"&libraryId="+Constant.LIB_CODE);
        return Constant.URL_READING+"/app/contestant/"+Constant.LIB_CODE+"/login?token=" +  Constant.URL_TOKEN + "&time=" + stamp+"&libraryId="+Constant.LIB_CODE;
    }


}
