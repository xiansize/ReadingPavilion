package com.tcsoft.read.activity;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tcsoft.read.R;
import com.tcsoft.read.manager.AppManager;
import com.tcsoft.read.utils.QrCodeUtil;

/**
 * 作品上传成功后的界面显示
 */
public class SucceedActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout rlBackPress;
    private ImageView ivShareCode;
    private String sharePath = "121212";
    private CountDownTimer countDownTimer;
    private TextView tvTitle,tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succeed);
        initView();
        init();
    }

    //初始化视图
    private void initView() {
        ivShareCode = (ImageView) findViewById(R.id.iv_share_code_succeed_activity);
        rlBackPress = (RelativeLayout) findViewById(R.id.rl_tb_background);
        rlBackPress.setOnClickListener(this);

        tvTitle = (TextView) findViewById(R.id.tv_title_name_title_bar);

        tvTitle.setText(R.string.text_succeed_activity_share_succeed);
        tvTime = (TextView) findViewById(R.id.tv_time_title_bar);


    }

    //初始化
    private void init() {
        sharePath = this.getIntent().getStringExtra("KEY_URL");
        Bitmap bitmap = QrCodeUtil.generateBitmap(sharePath,600,600);
        ivShareCode.setImageBitmap(bitmap);

        //倒计时
        countDownTimer = new CountDownTimer(60 * 1000, 1 * 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTime.setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                AppManager.finishAllActivity();
                switchActivity(SucceedActivity.this,LoginActivity.class,null);
            }
        };
        countDownTimer.start();
    }



    @Override
    public void onClick(View v) {
        //返回登陆页面
        AppManager.finishAllActivity();
        switchActivity(SucceedActivity.this,LoginActivity.class,null);
    }
}
