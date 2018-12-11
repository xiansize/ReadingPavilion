package com.tcsoft.read.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.tcsoft.read.R;
import com.tcsoft.read.action.implement.ContentActionImpl;
import com.tcsoft.read.action.implement.LoginActionImpl;
import com.tcsoft.read.action.interfaces.ContentAction;
import com.tcsoft.read.action.interfaces.LoginAction;
import com.tcsoft.read.callback.ActionListener;
import com.tcsoft.read.entity.Activity;
import com.tcsoft.read.receiver.TokenReceiver;
import com.tcsoft.read.utils.Constant;
import com.tcsoft.read.utils.NetUtil;
import com.tcsoft.read.utils.QrCodeUtil;
import com.tcsoft.read.utils.ToastUtil;
import com.tcsoft.read.view.HintDialog;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";


    private TextView tvShowTime;
    private RelativeLayout rlScanCodeLogin, rlInputCodeLogin;

    private HintDialog scanCodeDialog;
    private HintDialog inputCodeDialog;
    private HintDialog getOrderCodeDialog;


    private String qrCode;

    //读者输入的预约号
    private String mInputCode = "";

    private static final int MSG_TIME = 2;

    private LoginAction loginAction;
    private ContentAction contentAction;

    //登陆计时器
    private CountDownTimer countDownTimerLogin;

    //展示报名二维码计时器
    private CountDownTimer countDownTimerGetOrder;

    //输入预约码计时器
    private CountDownTimer countDownTimerCode;


    //跳转到屏保的计时器
    private CountDownTimer countDownTimerSleep;





    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_TIME:
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd  HH:mm");
                    tvShowTime.setText(simpleDateFormat.format(System.currentTimeMillis()));
                    break;
                default:

                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindowData();
        initView();
        init();
        repeatToken();




    }





    //初始化控件
    private void initView() {
        rlScanCodeLogin = (RelativeLayout) findViewById(R.id.rl_scan_code_login_activity);
        rlInputCodeLogin = (RelativeLayout) findViewById(R.id.rl_input_code_login_activity);

        rlScanCodeLogin.setOnClickListener(this);
        rlInputCodeLogin.setOnClickListener(this);

        tvShowTime = (TextView) findViewById(R.id.tv_show_time_login_activity);


    }





    //初始化
    private void init() {
        new TimeThread().start();
        loginAction = new LoginActionImpl();
        contentAction = new ContentActionImpl();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_input_code_login_activity:
                //计时休眠
                stopCountSleep();
                startCountSleep();
                //显示弹窗
                showDialogLogin(1);
                break;
            case R.id.rl_scan_code_login_activity:
//                //计时休眠
                stopCountSleep();
                startCountSleep();
                //显示弹窗
                showDialogLogin(0);
                break;
        }
    }



    //屏保时间
    private void startCountSleep(){
        countDownTimerSleep = new CountDownTimer(Constant.TIME_SLEEP*60*1000,10*1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                switchActivity(LoginActivity.this,WelcomeActivity.class);
            }
        };
        countDownTimerSleep.start();
    }


    //结束
    private void stopCountSleep(){
        countDownTimerSleep.cancel();
    }





    //输入预约码登陆
    private void inputCodeLogin() {
        if (mInputCode.equals("")) {
            return;
        }

        loginAction.inputCodeLogin(mInputCode, new ActionListener<String>() {
            @Override
            public void onSucceed(String s) {
                Constant.INPUT_CODE = mInputCode;
                mInputCode = "";
                inputCodeDialog.dismissDialog();
                switchActivity(LoginActivity.this, FunctionActivity.class, null);
            }

            @Override
            public void onFailed(String message) {
                ToastUtil.showToastShowTime(LoginActivity.this, message);
            }
        });
    }









    //读者进行扫码
    private void qrCodeLogin(String stamp) {
        loginAction.scanCodeLogin(stamp,new ActionListener<String>() {
            @Override
            public void onSucceed(String readId) {


                //停止循环
                countDownTimerLogin.cancel();
                scanCodeDialog.dismissDialog();
                //跳转
                Constant.RD_ID = readId;
                switchActivity(LoginActivity.this, FunctionActivity.class, null);

            }

            @Override
            public void onFailed(String message) {
                Log.d(TAG, message);
                ToastUtil.showToastShowTime(LoginActivity.this, message);
            }
        });
    }


    //判断读者是否有报名
    private void judgeOrderNum(final String readId) {
        loginAction.authOrderCode(readId, new ActionListener<String>() {
            @Override
            public void onSucceed(String type) {


                if (type.equals("1")) {

                    //读者扫码登陆成功
                    Constant.INPUT_CODE = null;
                    switchActivity(LoginActivity.this, FunctionActivity.class, null);

                } else {

                    //没有报名
                    qrCode = Constant.SIGHUP_PATH;
                    ToastUtil.showToastLongTime(LoginActivity.this, "请先报名方可参加活动");
                    getOrderCode();
                }
            }

            @Override
            public void onFailed(String message) {
                ToastUtil.showToastLongTime(LoginActivity.this, message);

            }
        });
    }


    //展示读者报名链接二维码
    private void getOrderCode() {
        //TODO
        showDialogLogin(2);

    }

    ;


    //获取token
    private void getToken() {
        loginAction.getToken(new ActionListener<String>() {
            @Override
            public void onSucceed(String result) {
                Log.d(TAG, "更新token");
                //selectActivity();
                getActivity();
            }

            @Override
            public void onFailed(String message) {
                ToastUtil.showToastShowTime(LoginActivity.this, message);
            }
        });
    }


    //获取活动
    private void getActivity(){
        contentAction.getActivity(new ActionListener<List<Activity>>() {
            @Override
            public void onSucceed(List<Activity> list) {
                //默认获取第一个活动
                Constant.ACTIVITY_ID = list.get(0).getaId();
            }

            @Override
            public void onFailed(String message) {
                ToastUtil.showToastShowTime(LoginActivity.this,message);

            }
        });
    }





    @Override
    protected void onResume() {
        getToken();
        startCountSleep();
        super.onResume();
    }


    @Override
    protected void onPause() {
        stopCountSleep();
        super.onPause();
    }


    //跳转到活动页面
    private void selectActivity(){
        switchActivity(this,ActivityActivity.class);
    }




    public class TimeThread extends Thread {
        @Override
        public void run() {
            super.run();
            do {
                try {
                    Thread.sleep(1000);
                    Message message = handler.obtainMessage();
                    message.what = MSG_TIME;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }


    //定时任务发送token请求
    private void repeatToken() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, TokenReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 50 * 60 * 1000, pendingIntent);
    }


    /**
     * 获取屏幕宽高
     */
    protected void getWindowData() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Constant.displayWidth = displayMetrics.widthPixels;
        Constant.displayHeight = displayMetrics.heightPixels;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "click backpress");
            return true;//return true;拦截事件传递,从而屏蔽back键。
        }
        if (KeyEvent.KEYCODE_HOME == keyCode) {
            Log.d(TAG, "home");
            return true;//
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 显示弹窗
     */
    private void showDialogLogin(int type) {

        //显示扫码登陆
        if (type == 0) {

            if (scanCodeDialog == null) {
                scanCodeDialog = new HintDialog(this, null, null);
            }
            scanCodeDialog.showDialog(R.layout.layout_dialog_qr_code_login, new HintDialog.IHintDialog() {
                @Override
                public void showWindowDetail(Window window, String title, String message) {
                    ImageView tvExit = (ImageView) window.findViewById(R.id.iv_exit_scan_code_dialog);
                    final ImageView tvQrCode = (ImageView) window.findViewById(R.id.iv_qr_scan_code_dialog);

                    //获取stamp
                    final String stamp = String.valueOf(System.currentTimeMillis());

                    //显示二维码
                    loginAction.getQrCode(stamp,new ActionListener<String>() {
                        @Override
                        public void onSucceed(String code) {
                            Log.d(TAG, code);
                            //动态显示二维码
                            LoginActivity.this.qrCode = code;
                            tvQrCode.setImageBitmap(QrCodeUtil.generateBitmap(qrCode, 600, 600));
                        }

                        @Override
                        public void onFailed(String message) {

                        }
                    });


                    countDownTimerLogin = new CountDownTimer(40 * 1000, 3 * 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            //if(IS_ACTION_SCAN)
                            qrCodeLogin(stamp);
                        }

                        @Override
                        public void onFinish() {
                            scanCodeDialog.dismissDialog();
                        }
                    };
                    countDownTimerLogin.start();




                    tvExit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            countDownTimerLogin.cancel();
                            scanCodeDialog.dismissDialog();
                        }
                    });

                }
            });


        } else if (type == 2) {

            //展示读者报名链接
            if (getOrderCodeDialog == null) {
                getOrderCodeDialog = new HintDialog(this, null, null);
            }
            getOrderCodeDialog.showDialog(R.layout.layout_dialog_qr_code_login, new HintDialog.IHintDialog() {
                @Override
                public void showWindowDetail(Window window, String title, String message) {
                    ImageView tvExit = (ImageView) window.findViewById(R.id.iv_exit_scan_code_dialog);
                    ImageView tvQrCode = (ImageView) window.findViewById(R.id.iv_qr_scan_code_dialog);

                    TextView tvTitle = (TextView) window.findViewById(R.id.tv_login_scan_qrcode);
                    tvTitle.setText("扫码报名");

                    TextView tvMessage = (TextView) window.findViewById(R.id.tv_message_login_scan_qrcode);
                    tvMessage.setText("打开微信扫描二维码报名");

                    tvQrCode.setImageBitmap(QrCodeUtil.generateBitmap(qrCode, 600, 600));

                    countDownTimerGetOrder = new CountDownTimer(40 * 1000, 3 * 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {


                        }

                        @Override
                        public void onFinish() {
                            getOrderCodeDialog.dismissDialog();
                        }
                    };
                    countDownTimerGetOrder.start();

                    tvExit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            countDownTimerGetOrder.cancel();
                            getOrderCodeDialog.dismissDialog();
                        }
                    });

                }
            });


        } else {

            //显示输入号码登陆
            if (inputCodeDialog == null) {
                inputCodeDialog = new HintDialog(this, null, null);
            }

            inputCodeDialog.showDialog(R.layout.layout_input_code_dialog, new HintDialog.IHintDialog() {
                @Override
                public void showWindowDetail(Window window, final String title, String message) {
                    ImageView ivExit = (ImageView) window.findViewById(R.id.iv_exit_input_code_dialog);
                    ivExit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mInputCode = "";
                            inputCodeDialog.dismissDialog();
                            countDownTimerCode.cancel();
                        }
                    });

                    final TextView tvInputCode = (TextView) window.findViewById(R.id.tv_input_code_code_dialog);

                    RelativeLayout rlNumber0 = (RelativeLayout) window.findViewById(R.id.rl_number_0_input_code_dialog);
                    rlNumber0.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mInputCode = mInputCode + "0";
                            tvInputCode.setText(mInputCode);
                        }
                    });

                    RelativeLayout rlNumber1 = (RelativeLayout) window.findViewById(R.id.rl_number_1_input_code_dialog);
                    rlNumber1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mInputCode = mInputCode + "1";
                            tvInputCode.setText(mInputCode);
                        }
                    });

                    RelativeLayout rlNumber2 = (RelativeLayout) window.findViewById(R.id.rl_number_2_input_code_dialog);
                    rlNumber2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mInputCode = mInputCode + "2";
                            tvInputCode.setText(mInputCode);
                        }
                    });

                    RelativeLayout rlNumber3 = (RelativeLayout) window.findViewById(R.id.rl_number_3_input_code_dialog);
                    rlNumber3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mInputCode = mInputCode + "3";
                            tvInputCode.setText(mInputCode);
                        }
                    });

                    RelativeLayout rlNumber4 = (RelativeLayout) window.findViewById(R.id.rl_number_4_input_code_dialog);
                    rlNumber4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mInputCode = mInputCode + "4";
                            tvInputCode.setText(mInputCode);
                        }
                    });

                    RelativeLayout rlNumber5 = (RelativeLayout) window.findViewById(R.id.rl_number_5_input_code_dialog);
                    rlNumber5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mInputCode = mInputCode + "5";
                            tvInputCode.setText(mInputCode);
                        }
                    });

                    RelativeLayout rlNumber6 = (RelativeLayout) window.findViewById(R.id.rl_number_6_input_code_dialog);
                    rlNumber6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mInputCode = mInputCode + "6";
                            tvInputCode.setText(mInputCode);
                        }
                    });

                    RelativeLayout rlNumber7 = (RelativeLayout) window.findViewById(R.id.rl_number_7_input_code_dialog);
                    rlNumber7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mInputCode = mInputCode + "7";
                            tvInputCode.setText(mInputCode);
                        }
                    });

                    RelativeLayout rlNumber8 = (RelativeLayout) window.findViewById(R.id.rl_number_8_input_code_dialog);
                    rlNumber8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mInputCode = mInputCode + "8";
                            tvInputCode.setText(mInputCode);
                        }
                    });

                    RelativeLayout rlNumber9 = (RelativeLayout) window.findViewById(R.id.rl_number_9_input_code_dialog);
                    rlNumber9.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mInputCode = mInputCode + "9";
                            tvInputCode.setText(mInputCode);
                        }
                    });

                    RelativeLayout rlNumberReset = (RelativeLayout) window.findViewById(R.id.rl_number_reset_input_code_dialog);
                    rlNumberReset.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mInputCode.length() > 0) {
                                mInputCode = mInputCode.substring(0, mInputCode.length() - 1);
                            }
                            tvInputCode.setText(mInputCode);
                        }
                    });
                    rlNumberReset.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            mInputCode = "";
                            tvInputCode.setText(mInputCode);
                            return false;
                        }
                    });


                    RelativeLayout rlNumberSure = (RelativeLayout) window.findViewById(R.id.rl_number_comfirm_input_code_dialog);
                    rlNumberSure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mInputCode.equals(Constant.OUT_KEY)) {
                                finish();
                            } else if (mInputCode.equals(Constant.VERSION_KEY)) {
                                switchActivity(LoginActivity.this, VersionActivity.class);
                            } else {
                                if (!mInputCode.equals("")) {
                                    inputCodeLogin();
                                }
                            }


                        }
                    });

                    //定时器
                    countDownTimerCode = new CountDownTimer(60*1000,10*1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            inputCodeDialog.dismissDialog();
                        }
                    };
                    countDownTimerCode.start();


                }
            });

        }
    }


}
