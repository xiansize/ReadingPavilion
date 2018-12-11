package com.tcsoft.read.action.implement;

import android.util.Log;

import com.google.gson.Gson;
import com.tcsoft.read.action.interfaces.LoginAction;
import com.tcsoft.read.api.implement.LoginApiImpl;
import com.tcsoft.read.api.interfaces.LoginApi;
import com.tcsoft.read.callback.ActionListener;
import com.tcsoft.read.thread.AsyncTaskAction;
import com.tcsoft.read.thread.AsyncTaskListener;
import com.tcsoft.read.utils.Constant;
import com.tcsoft.read.utils.Des;
import com.tcsoft.read.utils.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiansize on 2017/11/21.
 */
public class LoginActionImpl implements LoginAction {

    private static final String TAG = "LoginActionImpl";

    private LoginApi loginApi;
    private AsyncTaskAction asyncTaskAction;

    public LoginActionImpl() {
        this.loginApi = new LoginApiImpl();
    }

    @Override
    public void inputCodeLogin(final String code, final ActionListener<String> listener) {
        asyncTaskAction = new AsyncTaskAction(new AsyncTaskListener<String>() {
            @Override
            public String background() {
                return loginApi.inputCodeLogin(code);
            }

            @Override
            public void result(String result) {

                if (result != null) {
                    Log.d("Data", result);

                    try {
                        JSONObject json = new JSONObject(result);
                        if (json.getString("success").equals("true")) {
                            listener.onSucceed(null);
                        } else {
                            listener.onFailed("预约码不正确");
                        }
                    } catch (JSONException e) {
                        listener.onFailed("连接数据异常");
                        e.printStackTrace();
                    }
                } else {
                    listener.onFailed("连接服务器失败");
                }

            }
        });
        asyncTaskAction.execute();
    }


    @Override
    public void scanCodeLogin(final String stamp, final ActionListener<String> listener) {
        asyncTaskAction = new AsyncTaskAction(new AsyncTaskListener<String>() {
            @Override
            public String background() {
                return loginApi.scanQrCodeLogin(stamp);
            }

            @Override
            public void result(String result) {
                if (result != null) {
                    Log.d(TAG, result);

//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//                        String readId = jsonArray.getJSONObject(0).getString("rdid");
//                        listener.onSucceed(readId);
//
//                    } catch (JSONException e) {
//                        Log.d(TAG, "登陆异常");
//                        e.printStackTrace();
//                    }

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String readId = jsonObject.getString("card");
                        listener.onSucceed(readId);

                    } catch (JSONException e) {
                        Log.d(TAG, "登陆异常");
                        e.printStackTrace();
                    }


                } else {
                    listener.onFailed("连接服务器失败");
                }
            }
        });
        asyncTaskAction.execute();
    }


    @Override
    public void getQrCode(final String stamp, final ActionListener<String> listener) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("c", Constant.c);
//        map.put("a", Constant.a);
//        map.put("t", Constant.t);
//        map.put("u", Constant.u);
//        Map<String, String> d = new HashMap<>();
//        String serialNo = "qrcode" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(System.currentTimeMillis()) + ((int) Math.random() * 1000);
//        Constant.serialNo = serialNo;
//        d.put("serialNo", serialNo);
//        map.put("d", d);
//        Gson gson = new Gson();
//        String qrCode = gson.toJson(map);
//        //加密
//        String key = "376B4A409E5789CE";
//        Log.d(TAG, "没加密" + qrCode);
//        qrCode = Des.byteArrayToHexString(new Des().encrypt(qrCode, key).getBytes());
//        listener.onSucceed(qrCode);

        //接口更改为生成链接
        asyncTaskAction = new AsyncTaskAction(new AsyncTaskListener<String>() {
            @Override
            public String background() {
                return loginApi.getQrCode(stamp);
            }

            @Override
            public void result(String result) {

                if(result != null){

                    Log.d(TAG,result.toString());

                    listener.onSucceed(result);


                }else {
                    listener.onFailed("连接服务器失败");
                }

            }
        });
        asyncTaskAction.execute();



    }






    @Override
    public void getToken(final ActionListener<String> listener) {
        asyncTaskAction = new AsyncTaskAction(new AsyncTaskListener<String>() {
            @Override
            public String background() {
                return loginApi.getToken();
            }

            @Override
            public void result(String result) {



                if (result != null) {
                    Log.d(TAG, "getToken" + result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String succeed = jsonObject.getString("success");
                        if (succeed.equals("false")) {
                            listener.onFailed("更新服务器连接失败");
                        } else {
                            //获取token
                            Constant.URL_TOKEN = jsonObject.getString("token");
                            //获取ip
                            Constant.IP_ADDRESS = NetUtil.getHostIP();
                            listener.onSucceed(null);

                        }


                    } catch (JSONException e) {
                        listener.onFailed("连接服务器异常");
                    }
                } else {
                    listener.onFailed("连接服务器失败");
                }
            }
        });
        asyncTaskAction.execute();
    }


    @Override
    public void authOrderCode(final String readId, final ActionListener<String> listener) {
        asyncTaskAction = new AsyncTaskAction(new AsyncTaskListener<String>() {
            @Override
            public String background() {
                return loginApi.authOrderCode(readId);
            }

            @Override
            public void result(String result) {
                if (result != null) {

                    Log.d(TAG,result.toString());

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String succeed = jsonObject.getString("success");
                        if (succeed.equals("1")) {

                            listener.onSucceed(succeed);

                        } else {

                            listener.onSucceed("-2");

                        }


                    } catch (JSONException e) {
                        listener.onFailed("连接服务器异常");
                    }
                } else {
                    listener.onFailed("连接服务器失败");
                }


            }
        });
        asyncTaskAction.execute();
    }


}
