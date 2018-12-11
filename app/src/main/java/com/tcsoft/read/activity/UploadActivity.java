package com.tcsoft.read.activity;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tcsoft.read.R;
import com.tcsoft.read.action.implement.UploadActionImpl;
import com.tcsoft.read.action.interfaces.UploadAction;
import com.tcsoft.read.callback.ActionListener;
import com.tcsoft.read.manager.AppManager;
import com.tcsoft.read.utils.Constant;
import com.tcsoft.read.utils.MD5Util;
import com.tcsoft.read.utils.ToastUtil;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "UploadActivity";

    private RelativeLayout rlBackPress,rlUpload;

    private TextView tvTime;

    private TextView tvTitle;
    private EditText etTitle,etNote;

    private File sdcardfile = null;
    private String[] files;

    private UploadAction uploadAction;

    private CountDownTimer countDownTimer;

    private String artId;

    private String mPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
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


    private void timeCount(){
        tvTime = (TextView) findViewById(R.id.tv_time_title_bar);
        countDownTimer = new CountDownTimer(300*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTime.setText(millisUntilFinished/1000 + "");
            }

            @Override
            public void onFinish() {
                AppManager.finishAllActivity();
                switchActivity(UploadActivity.this,LoginActivity.class,null);
            }
        };
        countDownTimer.start();
    }


    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title_name_title_bar);
        tvTitle.setText(R.string.tv_upload_book_reading);

        rlBackPress = (RelativeLayout) findViewById(R.id.rl_backpress_title_bar);
        rlBackPress.setOnClickListener(this);

        rlUpload  = (RelativeLayout) findViewById(R.id.rl_upload_confirm_upload_activity);
        rlUpload.setOnClickListener(this);

        etTitle = (EditText) findViewById(R.id.et_file_name_upload_activity);
        etNote = (EditText) findViewById(R.id.et_note_upload_activity);

    }



    private void init(){
        uploadAction = new UploadActionImpl();
        artId = this.getIntent().getStringExtra(Constant.KEY_IS_FROM_ID);
        mPath = this.getIntent().getStringExtra(Constant.KEY_PATH);
    }




    private void uploadAudio(){
        String notes = etNote.getText().toString();
        String title = etTitle.getText().toString();
        if(notes.length() > 40){
            ToastUtil.showToastShowTime(this,"备注已超过规定字数40字");
            return;
        }
        if(title.length() > 15){
            ToastUtil.showToastShowTime(this,"标题已超过规定字数15字");
            return;
        }


        //上传文件
        Map<String,Object> map = new HashMap<>();
        if(Constant.RD_ID != null){
            map.put("rdCore",Constant.RD_ID);
        }
        map.put("address","atHome");
        map.put("activityId",Constant.ACTIVITY_ID);
        map.put("token",Constant.URL_TOKEN);
        map.put("file",new File(mPath));
        map.put("usDefTitle",title);
        if(Constant.INPUT_CODE != null){
            map.put("password", MD5Util.getMD5Security(Constant.INPUT_CODE));
        }
        map.put("data",notes);
        if(artId != null){
            map.put("articleId",artId);
        }
        uploadAction.uploadFile(Constant.URL_READING +"/file/upload", map, new ActionListener<String>() {
            @Override
            public void onSucceed(final String url) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //取消loading
                                dismissLoadingDialog();
                                //显示提示
                                ToastUtil.showToastShowTime(UploadActivity.this,"上传成功");
//                                if(artId != null){
//                                    AppManager.finishActivity(ScenarioShowActivity.class);
//                                }else{
//                                    AppManager.finishActivity(BookReadingActivity.class);
//                                }
//                                finish();

                                //跳转到登陆页面
//                                AppManager.finishAllActivity();
//                                switchActivity(UploadActivity.this,LoginActivity.class,null);

                                //跳转到成功界面
                                switchActivity(UploadActivity.this,SucceedActivity.class,"KEY_URL",url,null);

                            }
                        });
                    }
                }).start();

            }

            @Override
            public void onFailed(final String message) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //取消loading
                                dismissLoadingDialog();

                                //显示提示
                                ToastUtil.showToastShowTime(UploadActivity.this,message);
                            }
                        });
                    }
                }).start();
            }
        });
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_backpress_title_bar:
                finish();
                break;
            case R.id.rl_upload_confirm_upload_activity:
                //显示loading
                showLoadingDialog();
                //上传
                uploadAudio();
                break;
        }
    }










}
