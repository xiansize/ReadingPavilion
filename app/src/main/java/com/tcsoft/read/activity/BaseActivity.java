package com.tcsoft.read.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;
import com.tcsoft.read.R;
import com.tcsoft.read.manager.AppManager;
import com.tcsoft.read.utils.ToastUtil;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_base);
        AppManager.addActivity(this);

    }


    /**
     *
     * @param activity
     * @param tClass
     */
    protected void switchActivity(Activity activity,Class<? extends Activity> tClass){
        Intent intent = new Intent(activity,tClass);
        startActivity(intent);
    }

    /**
     *
     * @param activity
     * @param tClass
     */
    protected void switchActivity(Activity activity,Class<? extends Activity> tClass,String str){
        Intent intent = new Intent(activity,tClass);
        startActivity(intent);
        if(str==null)
            finish();
    }


    /**
     * 携带str跳转
     * @param activity
     * @param tClass
     * @param key
     * @param value
     * @param str
     */
    protected void switchActivity(Activity activity,Class<? extends Activity> tClass,String key,String value,String str){
        Intent intent = new Intent(activity,tClass);
        intent.putExtra(key,value);
        startActivity(intent);
        if(str==null)
            finish();
    }




    protected void switchActivity(Activity activity,Class<? extends Activity> tClass,String key,String value,String key1,boolean value2,String str){
        Intent intent = new Intent(activity,tClass);
        intent.putExtra(key,value);
        intent.putExtra(key1,value2);
        startActivity(intent);
        if(str==null)
            finish();
    }

    protected void switchActivity(Activity activity,Class<? extends Activity> tClass,String key,String value,String key1,String value2,String str){
        Intent intent = new Intent(activity,tClass);
        intent.putExtra(key,value);
        intent.putExtra(key1,value2);
        startActivity(intent);
        if(str==null)
            finish();
    }









    private AlertDialog alertDialog;

    public void showLoadingDialog() {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        alertDialog.setCancelable(false);
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK)
                    return true;
                return false;
            }
        });
        alertDialog.show();
        alertDialog.setContentView(R.layout.loading_alert);
        alertDialog.setCanceledOnTouchOutside(false);
    }

    public void dismissLoadingDialog() {
        if (null != alertDialog && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }





}
