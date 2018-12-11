package com.tcsoft.read.activity;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.tcsoft.read.R;
import com.tcsoft.read.manager.AppManager;
import com.tcsoft.read.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VoiceSearchActivity extends BaseActivity implements View.OnClickListener{

    public static final String TAG = "VoiceSearchActivity";

    private boolean isComing = false;

    private static final String APP_ID = "5a1d19dc";

    private TextView tvTime;

    private LinearLayout llJumpToWrite;
    private RelativeLayout rlBackPress;
    private ImageView ivTipsVoice,ivSearchAgain;
    private TextView tvTitle;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_search);
        initView();
        init();
    }

    private void init() {
        // 初始化识别对象
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "="+this.APP_ID);
    }

    @Override
    protected void onResume() {
        timeCount();
        showTalkDialog();
        super.onResume();
    }


    @Override
    protected void onPause() {
        countDownTimer.cancel();
        super.onPause();
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
                switchActivity(VoiceSearchActivity.this,LoginActivity.class,null);
            }
        };
        countDownTimer.start();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title_name_title_bar);
        tvTitle.setText(R.string.tv_voice_search);

        llJumpToWrite = (LinearLayout) findViewById(R.id.ll_jump_to_write_voice_search_activity);
        llJumpToWrite.setOnClickListener(this);

        rlBackPress = (RelativeLayout) findViewById(R.id.rl_backpress_title_bar);
        rlBackPress.setOnClickListener(this);

        ivTipsVoice = (ImageView) findViewById(R.id.iv_tips_voice_search_activity);

        ivSearchAgain = (ImageView) findViewById(R.id.iv_microphone_voice_search_activity);
        ivSearchAgain.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_jump_to_write_voice_search_activity:
                switchActivity(this,TitleSearchActivity.class,null);
                break;
            case R.id.rl_backpress_title_bar:
                finish();
                break;
            case R.id.iv_microphone_voice_search_activity:
                showTalkDialog();
                break;
        }
    }





    //开始讲话
    private void showTalkDialog(){
        //1.创建RecognizerDialog对象
        RecognizerDialog recognizerDialog = new RecognizerDialog(this, null);
        //2.设置accent、language等参数
        recognizerDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//语种，这里可以有zh_cn和en_us
        recognizerDialog.setParameter(SpeechConstant.ACCENT, "mandarin");//设置口音，这里设置的是汉语普通话 具体支持口音请查看讯飞文档，
        recognizerDialog.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");//设置编码类型



        //其他设置请参考文档http://www.xfyun.cn/doccenter/awd
        //3.设置讯飞识别语音后的回调监听
        recognizerDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(com.iflytek.cloud.RecognizerResult recognizerResult, boolean b) {
                String result = recognizerResult.getResultString();
                Log.d("OUTPUT",result);
                parseJson(result);

            }

            @Override
            public void onError(SpeechError speechError) {
                Log.d("OUTPUT","ERROR:"+speechError.getErrorCode() + " " +speechError.getMessage());
                if(speechError.getErrorCode() == 10118){
                    showTalkDialog();
                }
            }
        });
        //显示讯飞语音识别视图
        recognizerDialog.show();

    }




    //解析出语音识别到的汉字
    private String parseJson(String result){
        StringBuffer stringBuffer = new StringBuffer();

        try {
            JSONObject json = new JSONObject(result);
            JSONArray jsonArray = json.getJSONArray("ws");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("cw");
                JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                String w = jsonObject2.getString("w");
                stringBuffer.append(w);
            }

            String word = stringBuffer.toString();
            Log.d("OUTPUT",word);

            if("。".equals(word)){
                Log.d("ERROR",".");
            }else{
                sendToOtherActivity(word);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }


    //到其它页面进行查询
    private void sendToOtherActivity(String result){
        if(isComing == false){
            isComing = true;
            switchActivity(this,SearchResultActivity.class, Constant.KEY_TITLE,result,Constant.KEY_IS_FROM_ID,false,null);
        }
    }

}
