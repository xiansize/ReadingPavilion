package com.tcsoft.read.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by Administrator on 2018/2/26 0026.
 */

public class VersionUtil {

    /**
     * 获取版本好信息
     * @param context
     * @return
     */
    public static String getDeviceVersion(Context context){
        String versionName = "无版本信息";

        try {
            versionName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

}
