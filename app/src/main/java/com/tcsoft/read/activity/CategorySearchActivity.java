package com.tcsoft.read.activity;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tcsoft.read.R;
import com.tcsoft.read.action.implement.ContentActionImpl;
import com.tcsoft.read.action.interfaces.ContentAction;
import com.tcsoft.read.adapter.CategoryAdapter;
import com.tcsoft.read.callback.ActionListener;
import com.tcsoft.read.entity.Type;
import com.tcsoft.read.manager.AppManager;
import com.tcsoft.read.utils.Constant;
import com.tcsoft.read.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class CategorySearchActivity extends BaseActivity {

    public static String TAG = "CategorySearchActivity";

    private TextView tvTime;

    private RelativeLayout rlBackPress;

    private TextView tvTitle;
    private RecyclerView rvCategory;
    private StaggeredGridLayoutManager layoutManager;
    private CategoryAdapter categoryAdapter;
    private List<Type> mList;

    private ContentAction contentAction;

    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_search);
        initView();
        init();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title_name_title_bar);
        tvTitle.setText(R.string.tv_catgory_search);

        rlBackPress = (RelativeLayout) findViewById(R.id.rl_backpress_title_bar);
        rlBackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rvCategory = (RecyclerView) findViewById(R.id.rv_category_category_activity);
        layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL);
        rvCategory.setLayoutManager(layoutManager);
        mList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(mList,CategorySearchActivity.this);
        rvCategory.setAdapter(categoryAdapter);

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
                switchActivity(CategorySearchActivity.this,LoginActivity.class,null);
            }
        };
        countDownTimer.start();
    }


    private void init(){
        contentAction = new ContentActionImpl();
        contentAction.getCategory(new ActionListener<List<Type>>() {
            @Override
            public void onSucceed(List<Type> list) {
                mList.addAll(list);
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String message) {
                ToastUtil.showToastShowTime(CategorySearchActivity.this,message);
            }
        });


        categoryAdapter.setCategoryItemListener(new CategoryAdapter.CategoryItemListener() {
            @Override
            public void onClickItemListener(View view, int position) {
                switchActivity(CategorySearchActivity.this,SearchResultActivity.class, Constant.KEY_TITLE,mList.get(position).getTypeId()+"",Constant.KEY_IS_FROM_ID,true,"");
            }
        });

    }


}
