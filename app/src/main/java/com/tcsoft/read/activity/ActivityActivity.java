package com.tcsoft.read.activity;

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
import com.tcsoft.read.adapter.ActivityAdapter;
import com.tcsoft.read.callback.ActionListener;
import com.tcsoft.read.entity.Activity;
import com.tcsoft.read.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class ActivityActivity extends BaseActivity implements ActivityAdapter.ActivityItemListener{

    //TAG
    private static final String TAG = "ActivityActivity";

    //titleBar
    private TextView tvTime;
    private TextView tvTitle;

    //列表
    private RecyclerView rvActivity;

    //application
    private ContentAction contentAction;

    //list
    private List<Activity> mList;
    private ActivityAdapter activityAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);
        initView();
        init();
    }




    private void initView() {
        tvTime = (TextView) findViewById(R.id.tv_time_title_bar);
        tvTitle = (TextView) findViewById(R.id.tv_title_name_title_bar);
        tvTitle.setText("请选择活动");

        rvActivity = (RecyclerView) findViewById(R.id.rv_activity_activity);
        rvActivity.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        mList = new ArrayList<>();
        activityAdapter = new ActivityAdapter(mList,this);
        rvActivity.setAdapter(activityAdapter);

        contentAction = new ContentActionImpl();
    }


    private void init() {
        contentAction.getActivity(new ActionListener<List<Activity>>() {
            @Override
            public void onSucceed(List<Activity> list) {
                mList.addAll(list);
                activityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String message) {
                ToastUtil.showToastShowTime(ActivityActivity.this,message);

            }
        });

        activityAdapter.setActivityItemListener(this);

    }



    @Override
    public void onClickItemListener(View view, int position) {
        Log.d(TAG,mList.get(position).getaId() + mList.get(position).getaName());

    }


}
