package com.tcsoft.read.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiansize on 2017/11/23.
 */
public class Constant {

    //hangxin
    //public static final String URL_READING = "http://192.168.10.1:8087/Reading/webserver";
    //28099
    //public static final String URL_READING = "http://opac.interlib.com.cn:28098/Reading/webserver";
    //云端
    public static final String URL_READING = "https://cloud.dataesb.com/Reading";

    //图片地址
    public static final String IMG_PATH = "http://192.168.10.1:8087/reading/upload/poster/";


    //流水号接口
    public static final String URL_GET_SERIAL = "http://services.dataesb.com:81/onecard/interface/serial/getData";

    //ip地址
    public static String IP_ADDRESS = null;

    //存下token
    public static  String URL_TOKEN = null;
    //获取token的设备ID和密码
    public static final String DEVICE_ID = "654321";
    public static final String DEVICE_PASSWORD = "e10adc3949ba59abbe56e057f20f883e";

    //馆代码
    public static final String LIB_CODE= "wsinterlib";

    //活动id
    public static String ACTIVITY_ID = "9";

    //扫码登陆用到的图书馆token
    public static String LIB_TOKEN= "usrioi1443603333";


    //预约码
    public static String INPUT_CODE = null;

    //录制最长时间(分钟)
    public static final int COUNT_TOTAL_TIME = 5;


    //倒计时时间(秒)
    public static final int TIME_TOTAL = 50;


    //不操作显示屏保时间(分钟)
    public static final int TIME_SLEEP = 3;

    //退出桌面的密码
    public static final String OUT_KEY = "13352835767";
    //查看版本号
    public static final String VERSION_KEY = "15815817211";

    //报名地址
    public static final String SIGHUP_PATH = "https://u.interlib.cn/index.php?g=Wap&m=Newvote&a=index&token=usrioi1443603333&id=27";




    //平台接入
//    public static String MODEL_ID = ""; //模块id
//    public static String AUTH = ""; //使用md5加密进行校验,md5(model_id+密钥);
//    public static String OPEN_ID = "nfc";
    public static String RD_ID = null;//读者证号
//    public static String TOKEN = "usrioi1443603333";
//    public static String LIB_CODE = "";

    //二维码内容
    public static final String c = "TC_TEST_LIB";//全局馆代码
    public static final String a = "READING";//APP名称
    public static final String t = "2";//类型 1为URL跳转
    public static final String u = "http://interlib.com.cn/web/index.do";//请求地址
//    public static String id = "";//自定义参数
//    public static String name = "";//自定义参数
    public static  String serialNo = null;//唯一标识

//    public static String KEY = "$TC_2016@@WX";//TCSOFT$@SYLIB



    public static final String KEY_TITLE = "KEY_TITLE";
    public static final String KEY_IS_FROM_ID = "KEY_IS_FROM_ID";
    public static final String KEY_PATH = "KEY_PATH";
    public static final String VALUE_NULL = "";
    public static final String ACTION = "ACTION_TOKEN";

    public static int displayWidth;  //屏幕宽度
    public static int displayHeight; //屏幕高度


}
