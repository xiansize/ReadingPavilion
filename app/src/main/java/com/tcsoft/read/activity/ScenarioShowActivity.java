package com.tcsoft.read.activity;


import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tcsoft.read.R;
import com.tcsoft.read.action.implement.ContentActionImpl;
import com.tcsoft.read.action.interfaces.ContentAction;
import com.tcsoft.read.callback.ActionListener;
import com.tcsoft.read.entity.Art;
import com.tcsoft.read.manager.AppManager;
import com.tcsoft.read.utils.Constant;
import com.tcsoft.read.utils.ToastUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class ScenarioShowActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ScenarioShowActivity";

    private ContentAction contentAction;

    private RelativeLayout rlBackPress;

    private LinearLayout llPrelisten, llStart, llUpload;
    private TextView tvStart;
    private ImageView ivStart;

    private TextView tvTitle, tvSpeed, tvTotalPage, tvContent;

    private TextView tvTime;

    private String content;
    private int speed = 1;
    private int totlaPage = 1;

    private ImageView ivTurnLeft, ivTurnRight;

    private MediaPlayer mediaPlayer;
    private MediaRecorder recorder;
    private File sdcardfile = null;


    private CountDownTimer countDownTimer;

    private String mPath = null;

    //倒计时时间
    private static final int COUNT_TIME_TOTAL = 380;


    //时间提示
    private TextView tvHintTime;

    //录制时间计算
    private int countRecordTime = 0;
    private CountDownTimer recordCountTimer;
    private TextView tvRecordTime;

    //选中音乐
    private RelativeLayout rlMusicSelect;
    private TextView tvBakground;
    private String musicType;
    private String backgroundPath = null;
    private String defaultPath = null;

    //播放音乐
    private MediaPlayer mpMusic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_show);
        initView();
        init();
        getSDCardFile();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title_name_title_bar);

        rlBackPress = (RelativeLayout) findViewById(R.id.rl_backpress_title_bar);
        rlBackPress.setOnClickListener(this);

        tvContent = (TextView) findViewById(R.id.tv_content_show_show_activity);

        tvSpeed = (TextView) findViewById(R.id.tv_show_speed_scenario_show_activity);
        tvSpeed.setText(speed + "");

        tvTotalPage = (TextView) findViewById(R.id.tv_total_page_number_scenario_show_activity);
        tvTotalPage.setText("共 [ " + totlaPage + " ]页");

        ivTurnLeft = (ImageView) findViewById(R.id.iv_turn_left_scenario_show_activity);
        ivTurnRight = (ImageView) findViewById(R.id.iv_turn_right_scenario_show_activty);

        ivTurnLeft.setOnClickListener(this);
        ivTurnRight.setOnClickListener(this);

        llPrelisten = (LinearLayout) findViewById(R.id.ll_prelisten_scenario_reading_activity);
        llPrelisten.setEnabled(false);
        llStart = (LinearLayout) findViewById(R.id.ll_pause_scenario_reading_activity);

        llUpload = (LinearLayout) findViewById(R.id.ll_upload_scenario_reading_activity);
        llUpload.setEnabled(false);

        llPrelisten.setOnClickListener(this);
        llStart.setOnClickListener(this);
        llUpload.setOnClickListener(this);

        tvStart = (TextView) findViewById(R.id.tv_start_record_scenario_reading_activity);
        ivStart = (ImageView) findViewById(R.id.iv_start_record_scenario_reading_activity);

        tvHintTime = (TextView) findViewById(R.id.tv_tips_count_time_scenario_show_activity);
        tvHintTime.setText("*录制请不要超过" + Constant.COUNT_TOTAL_TIME + "分钟*");

        tvRecordTime = (TextView) findViewById(R.id.tv_count_time_scenario_show_activity);


        rlMusicSelect = (RelativeLayout) findViewById(R.id.rl_backround_select_scenario_show_activity);
        rlMusicSelect.setOnClickListener(this);
        tvBakground = (TextView) findViewById(R.id.tv_background_scenario_show_activity);

    }

    @Override
    protected void onResume() {
        timeCount();
        super.onResume();
    }


    @Override
    protected void onPause() {
        countDownTimer.cancel();
        stopBackgroundMusic();
        super.onPause();
    }


    private void timeCount() {
        tvTime = (TextView) findViewById(R.id.tv_time_title_bar);
        countDownTimer = new CountDownTimer(COUNT_TIME_TOTAL * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTime.setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                AppManager.finishAllActivity();
                switchActivity(ScenarioShowActivity.this, LoginActivity.class, null);
            }
        };
        countDownTimer.start();
    }

    private void init() {
        mediaPlayer = new MediaPlayer();

        content = this.getIntent().getStringExtra(Constant.KEY_TITLE);
        Log.d(TAG, content);

        contentAction = new ContentActionImpl();
        contentAction.getReadingContent(content, new ActionListener<Art>() {
            @Override
            public void onSucceed(Art art) {
                tvTitle.setText(art.getTitle());
                tvContent.setText(art.getContent());
                defaultPath = art.getMusic();

            }

            @Override
            public void onFailed(String message) {
                ToastUtil.showToastShowTime(ScenarioShowActivity.this, message);
            }
        });

    }

    //播放背景音乐
    private void showMusic() {
        //音乐播放
        mpMusic = new MediaPlayer();
        if (musicType.equals("无")) {

        } else {
            try {
                //设置资源
                mpMusic.setDataSource(backgroundPath);
                mpMusic.setAudioStreamType(AudioManager.STREAM_MUSIC);
                //设置重复
                mpMusic.setLooping(true);
                // 通过异步的方式装载媒体资源
                mpMusic.prepareAsync();
                mpMusic.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // 装载完毕回调
                        mpMusic.start();
                    }
                });
                mpMusic.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        //缓存错误
                        ToastUtil.showToastShowTime(ScenarioShowActivity.this, "背景音乐播放错误！");
                        return false;
                    }
                });
            } catch (IOException e) {
                ToastUtil.showToastShowTime(ScenarioShowActivity.this, "背景音乐播放失败");
            }

        }
    }


    //停止播放音乐
    private void stopBackgroundMusic() {
        if (mpMusic != null && mpMusic.isPlaying()) {
            mpMusic.stop();
            mpMusic.release();
            mpMusic = null;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_turn_left_scenario_show_activity:
                if (speed > 1)
                    tvSpeed.setText((--speed) + "");
                break;
            case R.id.iv_turn_right_scenario_show_activty:
                if (speed < 3)
                    tvSpeed.setText((++speed) + "");
                break;
            case R.id.rl_backpress_title_bar:
                finish();
                break;
            case R.id.ll_prelisten_scenario_reading_activity:
                ToastUtil.showToastShowTime(this, "播放录音..");
                playAudio(mPath);
                break;
            case R.id.ll_pause_scenario_reading_activity:
                switch (tvStart.getText().toString()) {
                    case "开始":
                        startRecord();
                        ivStart.setImageResource(R.drawable.icon_finish_reading);
                        tvStart.setText(R.string.button_finish_reading);
                        //计算时间
                        countTime();
                        //不能选中音乐
                        rlMusicSelect.setBackgroundResource(R.drawable.shape_code_grey);
                        break;
                    case "暂停":
                        ToastUtil.showToastShowTime(this, "暂停");
                        mediaPlayer.pause();
                        break;
                    case "完成":
                        //停止录音
                        stopRecord();
                        //更新UI
                        llPrelisten.setEnabled(true);
                        llPrelisten.setBackgroundResource(R.drawable.shape_code_purple);
                        llUpload.setEnabled(true);
                        llUpload.setBackgroundResource(R.drawable.shape_qr_code_login_button);
                        ivStart.setImageResource(R.drawable.icon_pause);
                        tvStart.setText(R.string.button_pause_reading);
                        //停止计算
                        recordCountTimer.cancel();
                        //停止播放音乐
                        stopBackgroundMusic();
                        break;
                }
                break;
            case R.id.ll_upload_scenario_reading_activity:
                switchActivity(this, UploadActivity.class, Constant.KEY_IS_FROM_ID, content, Constant.KEY_PATH, mPath, "");
                break;
            case R.id.rl_backround_select_scenario_show_activity:
                if (tvStart.getText().toString().equals("开始")) {
                    Intent intent = new Intent(this, BackgroundActivity.class);
                    intent.putExtra("flag", 1);
                    startActivityForResult(intent, 0x011);
                }
                break;
        }
    }


    //计算录制时间
    private void countTime() {
        if (recordCountTimer == null) {
            recordCountTimer = new CountDownTimer(Constant.COUNT_TOTAL_TIME * 60 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    //计算时间
                    ++countRecordTime;

                    //换算成分秒
                    int mis = countRecordTime / 60;
                    int sec = countRecordTime % 60;

                    //更新录制时间
                    if (sec < 10) {
                        tvRecordTime.setText("0" + mis + ":" + "0" + sec);
                    } else {
                        tvRecordTime.setText("0" + mis + ":" + sec);
                    }
                }

                @Override
                public void onFinish() {
                    //超出录制时间
                    Log.d(TAG, "超出时间");

                }
            };
        }
        recordCountTimer.start();

    }


    //开始录制
    private void startRecord() {
        if (recorder == null) {
            recorder = new MediaRecorder();
        }
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置音频源为手机麦克风
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//设置输出格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);//设置音频编码格式
        //获取内存卡的根目录，创建临时文件
        try {
            //File file=File.createTempFile("录音_",".aac",sdcardfile);
            mPath = sdcardfile.getPath() + "/" + System.currentTimeMillis() + ".mp3";
            File file = new File(mPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            recorder.setOutputFile(file.getAbsolutePath());//设置文件输出路径
            //准备和启动录制音频
            recorder.prepare();
            recorder.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //完成录音
    private void stopRecord() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
        //刷新列表数据
        //getFileList();
    }


    //获取sd卡路径
    private void getSDCardFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//内存卡存在
            sdcardfile = Environment.getExternalStorageDirectory();//获取目录文件
        } else {
            Toast.makeText(this, "未找到内存卡", Toast.LENGTH_SHORT).show();
        }
    }


    //预听录音
    private void playAudio(String path) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        //获取选中的背景音乐回调
        if (requestCode == 0x011 && resultCode == 0x022) {
            String path = intent.getStringExtra("path");
            String type = intent.getStringExtra("type");
            //设置音乐
            musicType = type;
            tvBakground.setText("背景音乐：" + musicType);
            if(musicType.equals("默认")){
                if (defaultPath != null) {
                    backgroundPath = Constant.URL_READING + "/music/download/" + defaultPath + "?token=" + Constant.URL_TOKEN;
                    showMusic();
                }else {
                    tvBakground.setText("背景音乐：" + "默认无");
                }
            }else{
                if (path != null) {
                    backgroundPath = Constant.URL_READING + "/music/download/" + path + "?token=" + Constant.URL_TOKEN;
                    showMusic();
                }
            }
        }
    }
}
