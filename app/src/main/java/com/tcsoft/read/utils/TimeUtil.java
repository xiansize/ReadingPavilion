package com.tcsoft.read.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiansize on 2017/11/21.
 */
public class TimeUtil {

    public static String getFormatTime(String time){

        Date date = new Date(Long.parseLong(time));
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        return format.format(date);
    }




}
