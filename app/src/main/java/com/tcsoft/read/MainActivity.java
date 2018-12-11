package com.tcsoft.read;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.tcsoft.read.activity.AudioActivity;
import com.tcsoft.read.activity.BaseActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout llWechatLogin,llLoginInput,llLoginType;
    private RelativeLayout rlLoginChangeType,rlInputCodeLogin,rlScanCodeLogin,rlInputCodeLoginOnclick;
    private ImageView ivWechatCode;
    private TextView tvWechatLoginTimeUp;
    private EditText etInputCode;

    private CountDownTimer countDownTimer;

    //秒单位
    private long countTime = 100;

    //二维码内容
    private static final String LINK_WECHAT_LOGIN = "http://www.sina.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //show();
    }


    private void initView() {

        llWechatLogin = (LinearLayout) findViewById(R.id.ll_wechat_login);
        llLoginType = (LinearLayout) findViewById(R.id.ll_login_type);

        llLoginInput = (LinearLayout) findViewById(R.id.ll_login_input_code);
        rlLoginChangeType = (RelativeLayout) findViewById(R.id.rl_change_login_type);

        rlInputCodeLogin = (RelativeLayout) findViewById(R.id.rl_input_code_login);
        rlScanCodeLogin = (RelativeLayout) findViewById(R.id.rl_scan_code_login);

        ivWechatCode = (ImageView) findViewById(R.id.iv_qr_code_login);
        tvWechatLoginTimeUp = (TextView) findViewById(R.id.tv_wechat_login_time_up);
        etInputCode = (EditText) findViewById(R.id.et_login_input_code);

        rlInputCodeLoginOnclick = (RelativeLayout) findViewById(R.id.rl_input_code_login_onclick);
        rlInputCodeLoginOnclick.setOnClickListener(this);


        rlLoginChangeType.setOnClickListener(this);

        rlInputCodeLogin.setOnClickListener(this);
        rlScanCodeLogin.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_change_login_type:
                showMainLogin();
                break;
            case R.id.rl_scan_code_login:
                showWechatLogin();
                break;
            case R.id.rl_input_code_login:
                showInputLogin();
                break;
            case R.id.rl_input_code_login_onclick:
//                String code = etInputCode.getText().toString().trim();
//                if(code != null && !"".equals(code)){
//                    login(code);
//                }else {
//                    Toast.makeText(MainActivity.this,"",Toast.LENGTH_SHORT).show();
//                }
                login(null);
                break;
        }
    }



    //显示微信登陆
    private void showWechatLogin(){
        closeInput();
        llLoginType.setVisibility(View.GONE);
        llWechatLogin.setVisibility(View.VISIBLE);
        ivWechatCode.setImageBitmap(generateBitmap(LINK_WECHAT_LOGIN,500,500));
        setTime();
    }


    //显示输入预约码登陆
    private void showInputLogin(){
        rlInputCodeLogin.setBackgroundColor(Color.parseColor("#CCCCCC"));
        rlInputCodeLogin.setEnabled(false);
        llLoginInput.setVisibility(View.VISIBLE);
        etInputCode.setFocusable(true);
    }


    //显示主页
    private void showMainLogin(){
        countDownTimer.cancel();
        llLoginType.setVisibility(View.VISIBLE);
        llWechatLogin.setVisibility(View.GONE);
    }



    /**
     * 输入预约码登陆
     * @param code
     */
    private void login(String code){
        switchActivity(this, AudioActivity.class);
    }




    /**
     * 显示倒计时
     */
    private void setTime(){
        countDownTimer = new CountDownTimer(countTime*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvWechatLoginTimeUp.setText(millisUntilFinished/1000+"s");
            }

            @Override
            public void onFinish() {
                showMainLogin();
            }
        };
        countDownTimer.start();
    }


    /**
     * 关闭软键盘
     */
    public void closeInput(){
        View view = this.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) this
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    /**
     * 得到二维码的位图bitmap
     * @param content 二维码的内容
     * @param width   宽度
     * @param height  高度
     * @return
     */
    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }



    private void show(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        Log.d("XIAN",screenWidth+"  "+screenHeight);
    }


}
