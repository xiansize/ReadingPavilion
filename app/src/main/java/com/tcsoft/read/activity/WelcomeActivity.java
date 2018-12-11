package com.tcsoft.read.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.tcsoft.read.R;

public class WelcomeActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout rlWallPaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
    }


    private void initView() {
        rlWallPaper = (RelativeLayout) findViewById(R.id.rl_wall_paper_welcome_activity);
        rlWallPaper.setBackgroundResource(R.drawable.fall);
        rlWallPaper.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_wall_paper_welcome_activity:
                finish();
                break;
        }
    }
}
