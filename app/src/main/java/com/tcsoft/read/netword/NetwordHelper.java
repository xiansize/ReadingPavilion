package com.tcsoft.read.netword;

import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by xiansize on 2017/11/22.
 */
public class NetwordHelper {


    private static boolean flag=false;
    private static final String TAG ="NetworkHelp";
    private static final int TIMEOUT_MILLIONS = 20000;



    /**
     * 上传文件到服务器
     */
    public static String uploadFile(String filePath, String  urlServer){
        //使用HttpClient



        return "";
    }





    /**
     * get请求
     * @param path
     * @return
     */
    public static String getDataByGet(String path){

        URL url =null;

        HttpURLConnection connection =null;

        InputStream inptStream =null;

        int responseCode;

        try {
            url = new URL(path);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(TIMEOUT_MILLIONS);
            connection.setConnectTimeout(TIMEOUT_MILLIONS);
            connection.setDoInput(true);
            connection.setUseCaches(false);

            responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                inptStream = connection.getInputStream();
                Log.d(TAG,"请求成功2");
                return dealResponseResult(inptStream);

            }

        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (connection != null) {
                    connection.disconnect();
                }
                if (inptStream != null) {
                    inptStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * get请求
     * @param path
     * @return
     */
    public static String getDataByGetEncode(String path,Map<String,String> params,String enc){

        URL url =null;

        HttpURLConnection connection =null;

        InputStream inptStream =null;

        int responseCode;

        try {

            StringBuilder sb = new StringBuilder(path);
            sb.append('?');
            for(Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), enc)).append('&');
            }
            sb.deleteCharAt(sb.length()-1);


            url = new URL(sb.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(TIMEOUT_MILLIONS);
            connection.setConnectTimeout(TIMEOUT_MILLIONS);
            connection.setDoInput(true);
            connection.setUseCaches(false);

            responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                inptStream = connection.getInputStream();
                Log.d(TAG,"请求成功2");
                return dealResponseResult(inptStream);

            }

        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (connection != null) {
                    connection.disconnect();
                }
                if (inptStream != null) {
                    inptStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }




    /**
     * post请求
     * @param params
     * @param path
     * @return
     */
    public static String sendDataByPost(Map<String, String> params, String path) {

        URL url=null;

        HttpURLConnection connection = null;

        OutputStream outputStream = null;

        InputStream inputStream = null;

        int responseCode;

        byte [] data = getRequestData(params).toString().getBytes();

        try {
            url = new URL(path);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(TIMEOUT_MILLIONS);
            connection.setReadTimeout(TIMEOUT_MILLIONS);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));

            outputStream = connection.getOutputStream();
            outputStream.write(data, 0, data.length);

            responseCode = connection.getResponseCode();

            if (responseCode == 200) {

                Log.d(TAG,"请求成功");
                inputStream = connection.getInputStream();
                return dealResponseResult(inputStream);
            }else{
                Log.d(TAG,"响应码："+responseCode);
            }
        } catch (Exception e) {

        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }





    /**
     * 将map解析出来成请求体
     * @param params
     * @return
     */
    public static StringBuffer getRequestData(Map<String, String> params) {
        StringBuffer buffer = new StringBuffer();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {

                buffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                        .append("&");
            }
            buffer.deleteCharAt(buffer.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }





    /**
     * json请求
     */
    public static String doJSonPost(String data,String path){
        URL url;
        HttpURLConnection connection = null;
        OutputStreamWriter writer = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            outputStream = connection.getOutputStream();
            writer = new OutputStreamWriter(outputStream);
            //发送参数
            writer.write(data);
            //清理当前编辑器的左右缓冲区，并使缓冲区数据写入基础流
            writer.flush();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String lines =reader.readLine();//读取请求结果
            if(connection.getResponseCode() == 200){
                inputStream = connection.getInputStream();
                return dealResponseResult(inputStream);
            }

            //reader.close();


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if(outputStream != null){
                    outputStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }





    /**
     * 处理返回结果
     * @param inputStream
     * @return
     */
    public static String dealResponseResult(InputStream inputStream) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte [] data = new byte[1024];
        int lenngth = 0;

        try {
            while ((lenngth = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, lenngth);
            }
            return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }






}
