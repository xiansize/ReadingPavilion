package com.tcsoft.read.activity;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tcsoft.read.R;
import com.tcsoft.read.manager.AppManager;
import com.tcsoft.read.utils.Constant;

public class TitleSearchActivity extends BaseActivity implements View.OnClickListener{

    public static final String TAG = "TitleSearchActivity";

    private TextView tvTime;

    private TextView tvTitle;
    private RelativeLayout rlJumpToVoice,rlBackPress,rlTitleSearch;

    private EditText etTitleName;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_search);
        initView();
    }

    @Override
    protected void onResume() {
        timeCount();
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
                switchActivity(TitleSearchActivity.this,LoginActivity.class,null);
            }
        };
        countDownTimer.start();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title_name_title_bar);
        tvTitle.setText(R.string.tv_title_search);

        rlBackPress = (RelativeLayout) findViewById(R.id.rl_backpress_title_bar);
        rlBackPress.setOnClickListener(this);

        rlTitleSearch = (RelativeLayout) findViewById(R.id.rl_confirm_title_search_activity);
        rlTitleSearch.setOnClickListener(this);

        rlJumpToVoice = (RelativeLayout) findViewById(R.id.rl_jump_to_voice_title_search_activity);
        rlJumpToVoice.setOnClickListener(this);

        etTitleName = (EditText) findViewById(R.id.et_title_name_title_search_activity);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_jump_to_voice_title_search_activity:
                switchActivity(this,VoiceSearchActivity.class,null);
                break;
            case R.id.rl_backpress_title_bar:
                finish();
                break;
            case R.id.rl_confirm_title_search_activity:
                String title = etTitleName.getText().toString();
                switchActivity(this,SearchResultActivity.class, Constant.KEY_TITLE,title,Constant.KEY_IS_FROM_ID,false,"");
                break;
        }
    }
}
