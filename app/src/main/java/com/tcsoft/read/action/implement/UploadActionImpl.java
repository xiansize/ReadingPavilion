package com.tcsoft.read.action.implement;


import android.util.Log;
import com.tcsoft.read.action.interfaces.UploadAction;
import com.tcsoft.read.callback.ActionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xiansize on 2017/11/22.
 */
public class UploadActionImpl implements UploadAction {


    private static final String TAG = "UploadActionImpl";

    @Override
    public void uploadFile(String requestUrl, Map<String, Object> paramsMap, final ActionListener<String> listener) {

        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MultipartBody.FORM);
            //追加参数
            for (String key : paramsMap.keySet()) {
                Object object = paramsMap.get(key);
                if (!(object instanceof File)) {
                    builder.addFormDataPart(key, object.toString());
                } else {
                    File file = (File) object;
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(null, file));//MediaType.parse("text/x-markdown; charset=utf-8")：null的参数可以为
                    //Log.d(TAG,key+" AND "+file.getAbsolutePath());
                }
            }
            //创建RequestBody
            RequestBody body = builder.build();
            //创建Request
            final Request request = new Request.Builder().url(requestUrl).post(body).build();
            final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
            OkHttpClient okHttpClient  = httpBuilder
                    //设置超时
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "uploadMultiFile() e=" + e);
                    listener.onFailed("上传失败:"+e.toString());
                }


                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if(response.isSuccessful()){

                        String json = response.body().string();
                        Log.d(TAG,"response:" + json);

                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if(jsonObject.getString("success").equals("true")){
                                    //获取url播放地址
                                    String url = jsonObject.getString("url");
                                    listener.onSucceed(url);
                                }else{
                                    listener.onFailed("上传失败！");
                                }

                            } catch (JSONException e) {
                                listener.onFailed("上传响应异常！");
                            }

                    }else{
                        listener.onFailed("上传出错！请检查网络");
                    }
                }
            });
        } catch (Exception e) {
            //Log.d(TAG, "上传失败:"+e.getMessage());
            listener.onFailed("上传异常:"+e.getMessage());
        }
    }
}
