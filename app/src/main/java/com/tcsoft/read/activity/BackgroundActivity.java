package com.tcsoft.read.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tcsoft.read.R;
import com.tcsoft.read.action.implement.ContentActionImpl;
import com.tcsoft.read.action.interfaces.ContentAction;
import com.tcsoft.read.adapter.BackgroundAdapter;
import com.tcsoft.read.callback.ActionListener;
import com.tcsoft.read.entity.Background;
import com.tcsoft.read.manager.AppManager;
import com.tcsoft.read.utils.Constant;
import com.tcsoft.read.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class BackgroundActivity extends BaseActivity implements BackgroundAdapter.BackgroundItemListener, View.OnClickListener {


    //view
    private TextView tvTitle;
    private RecyclerView rvBackground;
    private BackgroundAdapter backgroundAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //data
    private ContentAction contentAction;
    private List<Background> mList;


    //倒计时
    private TextView tvTime;
    private CountDownTimer countDownTimer;
    private RelativeLayout rlBackpress;



    //来自带书朗读还是场景朗读
    private int flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        initView();
        init();

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


    //初始化初始化view
    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title_name_title_bar);
        tvTitle.setText(R.string.tv_background);

        rvBackground = (RecyclerView) findViewById(R.id.rv_background_activity);
        layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        rvBackground.setLayoutManager(layoutManager);

        mList = new ArrayList<>();
        backgroundAdapter = new BackgroundAdapter(mList, this);
        rvBackground.setAdapter(backgroundAdapter);

        rlBackpress = (RelativeLayout) findViewById(R.id.rl_backpress_title_bar);
        rlBackpress.setOnClickListener(this);
    }


    //数据绑定
    private void init() {
        contentAction = new ContentActionImpl();
        contentAction.getBackground(new ActionListener<List<Background>>() {
            @Override
            public void onSucceed(List<Background> list) {
                mList.addAll(list);
                backgroundAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String message) {
                ToastUtil.showToastShowTime(BackgroundActivity.this, message);
            }
        });


        backgroundAdapter.setBackgroundItemListener(this);

    }


    private void timeCount() {
        tvTime = (TextView) findViewById(R.id.tv_time_title_bar);
        countDownTimer = new CountDownTimer(Constant.TIME_TOTAL * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTime.setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                AppManager.finishAllActivity();
                switchActivity(BackgroundActivity.this, LoginActivity.class, null);
            }
        };
        countDownTimer.start();
    }


    @Override
    public void onClickItemListener(View view, int position) {
        if (position == 0) {
            //无背景音乐
            selectTo(flag, null, "无");


        } else if (position == 1) {
            //默认背景音乐
            selectTo(flag, mList.get(2).getMusicpPath(), "默认");


        } else {
            //其它音乐
            selectTo(flag, mList.get(position).getMusicpPath(), mList.get(position).getMusicType());


        }
    }


    //选中跳转
    private void selectTo(int flag, String musicPath, String musicType) {
        Intent intent = new Intent();
        intent.putExtra("path", musicPath);
        intent.putExtra("type", musicType);

        //回调
        setResult(0x022, intent);
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_backpress_title_bar:
                finish();
                break;
        }
    }
}
