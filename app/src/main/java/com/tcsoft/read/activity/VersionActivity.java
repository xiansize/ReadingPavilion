package com.tcsoft.read.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcsoft.read.R;
import com.tcsoft.read.utils.VersionUtil;

public class VersionActivity extends BaseActivity implements View.OnClickListener{

    private TextView tvTime,tvVersion;
    private LinearLayout llVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        initView();
        init();
    }


    private void init() {
        tvVersion.setText("版本："+VersionUtil.getDeviceVersion(this));
    }

    private void initView() {
        tvTime = (TextView) findViewById(R.id.tv_time_version_activity);
        tvVersion = (TextView) findViewById(R.id.tv_version_version_activity);

        llVersion = (LinearLayout) findViewById(R.id.ll_finish_version_activity);
        llVersion.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        finish();
    }
}
