package com.tcsoft.read.activity;



import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tcsoft.read.R;
import com.tcsoft.read.manager.AppManager;
import com.tcsoft.read.utils.Constant;

public class ScenarioReadingActivity extends BaseActivity implements View.OnClickListener{

    private TextView tvTitle;

    private TextView tvTime;

    private LinearLayout llTitleSearch,llVoiceSearch,llCategorySearch;

    private RelativeLayout rlBackpress;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_reading);
        initView();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title_name_title_bar);
        tvTitle.setText(R.string.text_view_scenario_function_activity);

        rlBackpress = (RelativeLayout) findViewById(R.id.rl_backpress_title_bar);
        rlBackpress.setOnClickListener(this);

        llTitleSearch = (LinearLayout) findViewById(R.id.ll_title_search_scenario_activity);
        llTitleSearch.setOnClickListener(this);

        llVoiceSearch = (LinearLayout) findViewById(R.id.ll_voice_search_scenario_activity);
        llVoiceSearch.setOnClickListener(this);

        llCategorySearch = (LinearLayout) findViewById(R.id.ll_category_search_scenario_activity);
        llCategorySearch.setOnClickListener(this);


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
                switchActivity(ScenarioReadingActivity.this,LoginActivity.class,null);
            }
        };
        countDownTimer.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_search_scenario_activity:
                switchActivity(this,TitleSearchActivity.class);
                break;
            case R.id.ll_voice_search_scenario_activity:
                switchActivity(this,VoiceSearchActivity.class);
                break;
            case R.id.ll_category_search_scenario_activity:
                switchActivity(this,CategorySearchActivity.class);
                break;
            case R.id.rl_backpress_title_bar:
                finish();
                break;
        }
    }



}