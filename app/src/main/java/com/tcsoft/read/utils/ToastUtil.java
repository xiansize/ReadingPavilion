package com.tcsoft.read.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by xiansize on 2017/11/24.
 */
public class ToastUtil {

    public static void showToastShowTime(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

    }


    public static void showToastLongTime(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();

    }


}
