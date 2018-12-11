package com.tcsoft.read.activity;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tcsoft.read.R;
import com.tcsoft.read.action.implement.ContentActionImpl;
import com.tcsoft.read.action.interfaces.ContentAction;
import com.tcsoft.read.adapter.SearchResultAdapter;
import com.tcsoft.read.callback.ActionListener;
import com.tcsoft.read.entity.Art;
import com.tcsoft.read.manager.AppManager;
import com.tcsoft.read.manager.ReadingGridLayoutManager;
import com.tcsoft.read.utils.Constant;
import com.tcsoft.read.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SearchResultActivity";

    private TextView tvTime;

    private RelativeLayout rlBackPress;

    private ImageView ivTurnLeft, ivTurnRight;

    private TextView tvTitle, tvPage, tvNoResult;


    private String searchTitle = null;
    private String searchId = null;

    private RecyclerView rvResult;
    private SearchResultAdapter searchResultAdapter;
    private ReadingGridLayoutManager readingGridLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private List<Art> mList;
    private List<Art> totalList;

    private ContentAction contentAction;

    private int currentPage = 1;
    private int totalPage = 1;


    private CountDownTimer countDownTimer;

    private LinearLayout llNoResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        initView();
        init();

    }


    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title_name_title_bar);
        tvTitle.setText(R.string.tv_search_result);

        tvTime = (TextView) findViewById(R.id.tv_time_title_bar);


        llNoResult = (LinearLayout) findViewById(R.id.ll_no_search_result_search_result_activity);
        tvNoResult = (TextView) findViewById(R.id.tv_no_result_search_result_activity);

        rlBackPress = (RelativeLayout) findViewById(R.id.rl_backpress_title_bar);
        rlBackPress.setOnClickListener(this);

        tvPage = (TextView) findViewById(R.id.tv_total_number_search_result_activity);

        ivTurnLeft = (ImageView) findViewById(R.id.iv_turn_left_search_result_activity);
        ivTurnRight = (ImageView) findViewById(R.id.iv_turn_right_search_result_activity);
        ivTurnLeft.setOnClickListener(this);
        ivTurnRight.setOnClickListener(this);


        rvResult = (RecyclerView) findViewById(R.id.rv_search_resutl_search_result_activity);
        readingGridLayoutManager = new ReadingGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvResult.setLayoutManager(readingGridLayoutManager);
        mList = new ArrayList<>();
        totalList = new ArrayList<>();
        searchResultAdapter = new SearchResultAdapter(mList, this);
        rvResult.setAdapter(searchResultAdapter);


    }


    private void init() {
        if (this.getIntent().getBooleanExtra(Constant.KEY_IS_FROM_ID, false)) {
            searchId = this.getIntent().getStringExtra(Constant.KEY_TITLE);
            Log.d(TAG, "id" + searchId);
        } else {
            searchTitle = this.getIntent().getStringExtra(Constant.KEY_TITLE);
            Log.d(TAG, "title" + searchTitle);
        }

        //加载数据
        contentAction = new ContentActionImpl();
        pageSearch(currentPage);


        searchResultAdapter.setSearchResultItemListener(new SearchResultAdapter.SearchResultItemListener() {
            @Override
            public void SearchResultItemListener(View view, int position) {
                countDownTimer.cancel();
                switchActivity(SearchResultActivity.this, ScenarioShowActivity.class, Constant.KEY_TITLE, mList.get(position).getArtId(), "");
            }
        });


    }


    //分页加载
    private void pageSearch(int page) {
        contentAction.getContentList(page + "", searchId, searchTitle, new ActionListener<List<Art>>() {
            @Override
            public void onSucceed(List<Art> arts) {
                if (arts.size() == 0) {
                    llNoResult.setVisibility(View.VISIBLE);
                    tvNoResult.setText("没有关于“" + searchTitle + "”的文章");
                }else{
                    //显示数据
                    if (mList.size() > 0) {
                        mList.clear();
                    }
                    mList.addAll(arts);
                    searchResultAdapter.notifyDataSetChanged();

                    //显示几页
                    currentPage = mList.get(0).getcPage();
                    totalPage = mList.get(0).gettPage();
                    tvPage.setText(currentPage + "  /  " + totalPage);
                }




            }

            @Override
            public void onFailed(String message) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_turn_left_search_result_activity:
                if (currentPage > 1) {
                    pageSearch(--currentPage);
                }
                break;
            case R.id.iv_turn_right_search_result_activity:
                if (totalPage > currentPage) {
                    pageSearch(++currentPage);
                }
                break;
            case R.id.rl_backpress_title_bar:
                finish();
                break;

        }
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


    private void timeCount() {
        tvTime = (TextView) findViewById(R.id.tv_time_title_bar);
        countDownTimer = new CountDownTimer(100 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTime.setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                AppManager.finishAllActivity();
                switchActivity(SearchResultActivity.this, LoginActivity.class, null);
            }
        };
        countDownTimer.start();
    }


}
