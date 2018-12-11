package com.tcsoft.read.activity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tcsoft.read.R;
import com.tcsoft.read.action.implement.UploadActionImpl;
import com.tcsoft.read.action.interfaces.UploadAction;
import com.tcsoft.read.callback.ActionListener;
import com.tcsoft.read.utils.Constant;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AudioActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout rlShowTips,rlRecordFinish,rlRecordRelisten,rlRecordUpload;
    private LinearLayout llInputFileName;
    private EditText etInputFileName;
    private TextView tvRecordingTips;
    private Button btnRecordingPrepare,btnUploadRecord;

    private CountDownTimer countDownTimer;

    private MediaPlayer mediaPlayer;

    private File sdcardfile = null;
    private String[] files;
    private MediaRecorder recorder=null;

    private TextView tvRelisten;
    private TextView tvRecordFinish;

    private String fileName = "音频-"+System.currentTimeMillis();

    private UploadAction uploadAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        initView();
        init();
    }




    private void init() {
        mediaPlayer = new MediaPlayer();
        getSDCardFile();
        getFileList();

        uploadAction = new UploadActionImpl();
    }


    private void initView() {
        rlShowTips = (RelativeLayout) findViewById(R.id.rl_show_tips_first);
        btnRecordingPrepare = (Button) findViewById(R.id.btn_prepare_recording);
        tvRecordingTips = (TextView) findViewById(R.id.tv_recording_tips);

        btnRecordingPrepare.setOnClickListener(this);

        rlRecordFinish = (RelativeLayout) findViewById(R.id.rl_record_finish);
        rlRecordFinish.setOnClickListener(this);

        rlRecordRelisten = (RelativeLayout) findViewById(R.id.rl_record_relisten);
        rlRecordRelisten.setOnClickListener(this);

        rlRecordUpload = (RelativeLayout) findViewById(R.id.rl_record_upload);
        rlRecordUpload.setOnClickListener(this);

        tvRelisten = (TextView) findViewById(R.id.tv_relisten);
        tvRecordFinish = (TextView) findViewById(R.id.tv_record_finish);

        llInputFileName = (LinearLayout) findViewById(R.id.ll_input_file_name);
        etInputFileName = (EditText) findViewById(R.id.et_input_file_name);

        btnUploadRecord = (Button) findViewById(R.id.btn_upload_record);
        btnUploadRecord.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_prepare_recording:
//                rlShowTips.setVisibility(View.GONE);
//                //倒计时录制开始开始
//                tvRecordingTips.setVisibility(View.VISIBLE);
//                setTimer();
                uploadAudio();
                break;
            case R.id.rl_record_finish:
                //完成录音
                stopRecord();
                tvRecordingTips.setText("录音完成！");
                rlRecordFinish.setVisibility(View.GONE);
                rlRecordUpload.setVisibility(View.VISIBLE);
                rlRecordRelisten.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_record_relisten:
                if(tvRelisten.getText().toString().equals("预听")){
                    tvRelisten.setText("停止");
                    tvRecordingTips.setText("播放录音");
                    playAudio(files[files.length-1]);
                }else{
                    tvRecordingTips.setText("播放停止");
                    tvRelisten.setText("预听");
                    mediaPlayer.pause();
                }
                break;
            case R.id.rl_record_upload:
                llInputFileName.setVisibility(View.VISIBLE);
                rlRecordRelisten.setVisibility(View.GONE);
                rlRecordUpload.setVisibility(View.GONE);
                if(mediaPlayer!= null && mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                tvRecordingTips.setText("上传文件");
                break;
            case R.id.btn_upload_record:
                uploadAudio();
                break;
        }
    }



    //上传录音
    private void uploadAudio() {
        //修改名称
        String srcFile = sdcardfile.getAbsoluteFile()+File.separator+files[files.length-1];
        fileName = etInputFileName.getText().toString().trim();
        String destFile = sdcardfile.getAbsoluteFile()+File.separator+fileName+".amr";
        boolean isDone = renameFile(srcFile,destFile);
        Log.d("XIAN","isDone: "+isDone);

        //上传文件
        Map<String,Object> map = new HashMap<>();
        map.put("file",new File(sdcardfile.getAbsoluteFile()+File.separator+fileName+".amr"));
        uploadAction.uploadFile(Constant.URL_READING +"/file/upload", map, new ActionListener<String>() {
            @Override
            public void onSucceed(String s) {
                //Toast.makeText(AudioActivity.this,"onSucceed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String message) {
                //Toast.makeText(AudioActivity.this,"failde",Toast.LENGTH_SHORT).show();
            }
        });
    }


    //获取sd卡路径
    private void getSDCardFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//内存卡存在
            sdcardfile= Environment.getExternalStorageDirectory();//获取目录文件
        }else {
            Toast.makeText(this,"未找到内存卡", Toast.LENGTH_SHORT).show();
        }
    }



    //倒计时
    private void setTimer() {
        countDownTimer = new CountDownTimer(8*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                switch ((int) millisUntilFinished / 1000){
                    case 7: tvRecordingTips.setText("倒计时准备...");
                        break;
                    case 4: tvRecordingTips.setText("3");
                        break;
                    case 3: tvRecordingTips.setText("2");
                        break;
                    case 2: tvRecordingTips.setText("1");
                        break;
                    case 1: tvRecordingTips.setText("请开始");
                        break;
                }
            }

            @Override
            public void onFinish() {
                tvRecordingTips.setText("录制中...");
                startRecord();
                rlRecordFinish.setVisibility(View.VISIBLE);
            }
        };
        countDownTimer.start();
    }



    //开始录制
    private void startRecord(){
        if(recorder==null){
            recorder=new MediaRecorder();
        }
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置音频源为手机麦克风
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//设置输出格式3gp
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//设置音频编码为amr格式
        //获取内存卡的根目录，创建临时文件
        try {
            File file=File.createTempFile("录音_",".amr",sdcardfile);
            recorder.setOutputFile(file.getAbsolutePath());//设置文件输出路径
            //准备和启动录制音频
            recorder.prepare();
            recorder.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    //完成录音
    private void stopRecord(){
        if(recorder!=null){
            recorder.stop();
            recorder.release();
            recorder=null;
        }
        //刷新列表数据
        getFileList();
    }



    //获取录音文件
    private void getFileList(){
        if(sdcardfile!=null){
            files=sdcardfile.list(new MyFilter());
        }
    }



    //定义一个文件过滤器MyFilter的内部类,实现FilenameFilter接口重写里边accept方法
    class MyFilter implements FilenameFilter {
        @Override
        public boolean accept(File pathname,String fileName) {
            return fileName.endsWith(".amr");
        }
    }



    //预听录音
    private void playAudio(String path)  {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(sdcardfile.getAbsoluteFile()+File.separator+path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    tvRecordingTips.setText("播放结束");
                    tvRelisten.setText("预听");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //修改文件名
    private boolean renameFile(String src,String dest){
        boolean isDone = false;
        File srcDir = new File(src);  //就文件夹路径
        isDone = srcDir.renameTo(new File(dest));
        return isDone;
    }

}
