package com.tcsoft.read.activity;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tcsoft.read.R;
import com.tcsoft.read.adapter.BackgroundAdapter;
import com.tcsoft.read.entity.Background;
import com.tcsoft.read.manager.AppManager;
import com.tcsoft.read.utils.Constant;
import com.tcsoft.read.utils.HintPlay;
import com.tcsoft.read.view.HintDialog;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Handler;

public class FunctionActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "FunctionActivity";

    private LinearLayout llBookReading,llScenarioReading;

    private RelativeLayout rlBackPress;

    private TextView tvTime;

    private CountDownTimer countDownTimer;

    private HintDialog hintDialog;

    private HintPlay hintPlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        initView();
        init();
    }

    private void init() {
        playHintVoice();
    }


    @Override
    protected void onResume() {

        timeCount();
        super.onResume();
    }


    @Override
    protected void onPause() {
        hintPlay.stopHintVoice();
        countDownTimer.cancel();
        super.onPause();
    }





    private void playHintVoice(){
        hintPlay = new HintPlay(this);
        hintPlay.setResource(R.raw.login_finish,null);
        hintPlay.playHintVoice();
    }


    private void timeCount(){
        tvTime = (TextView) findViewById(R.id.tv_time_title_bar);
        countDownTimer = new CountDownTimer(Constant.TIME_TOTAL*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTime.setText(millisUntilFinished/1000 + "");
            }

            @Override
            public void onFinish() {
                AppManager.finishAllActivity();
                switchActivity(FunctionActivity.this,LoginActivity.class,null);
            }
        };
        countDownTimer.start();
    }




    private void initView() {
        llBookReading = (LinearLayout) findViewById(R.id.ll_book_reading_function_reading);
        llScenarioReading = (LinearLayout) findViewById(R.id.ll_scenario_reading_function_activity);

        llBookReading.setOnClickListener(this);
        llScenarioReading.setOnClickListener(this);


        rlBackPress = (RelativeLayout) findViewById(R.id.rl_backpress_title_bar);
        rlBackPress.setOnClickListener(this);

    }








    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_book_reading_function_reading:
                switchActivity(this,BookReadingActivity.class);
                break;
            case R.id.ll_scenario_reading_function_activity:
                switchActivity(this,ScenarioReadingActivity.class);
                break;
            case R.id.rl_backpress_title_bar:
                //showHintDialog();
                switchActivity(FunctionActivity.this,LoginActivity.class,null);
                break;
        }
    }



    private void showHintDialog(){
        if(hintDialog == null){
            hintDialog = new HintDialog(this,null,null);
        }
        hintDialog.showDialog(R.layout.layout_dialog_confirm, new HintDialog.IHintDialog() {
            @Override
            public void showWindowDetail(Window window, String title, String message) {
                RelativeLayout rlConfirm = (RelativeLayout) window.findViewById(R.id.rl_confirm_hint_dialog);
                ImageView ivCancel = (ImageView) window.findViewById(R.id.iv_cancel_hint_dialog);

                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hintDialog.dismissDialog();
                    }
                });

                rlConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchActivity(FunctionActivity.this,LoginActivity.class,null);
                    }
                });


            }
        });

    }


}
