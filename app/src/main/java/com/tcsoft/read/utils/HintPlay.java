package com.tcsoft.read.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.tcsoft.read.R;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by xiansize on 2017/12/11.
 */
public class HintPlay {

    private static final String TAG = "HintPlay";


    private MediaPlayer mediaPlayer;
    private Context context;

    public HintPlay(Context context) {
        this.context = context;
        this.mediaPlayer = getMediaPlayer(context);
    }


    //设置资源
    public void setResource(int resource,String path){

        if(path == null){
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(resource);
            try {
                mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                afd.close();
                mediaPlayer.prepare();

            } catch (IOException e) {
                Log.d(TAG,"获取播放数据源异常");
                e.printStackTrace();
            }
        }else{
            try {
                mediaPlayer.setDataSource(context, Uri.parse(path));
                mediaPlayer.prepare();

            } catch (IOException e) {
                Log.d(TAG,"获取播放数据源异常");
                e.printStackTrace();
            }
        }


    }


    //开始播放
    public void playHintVoice(){
        mediaPlayer.start();

    }


    //结束播放
    public void stopHintVoice(){
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    //设置监听
    public void setHintPlayListener(final HintPlayFinishListener hintPlayFinishListener){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                hintPlayFinishListener.playFinish(mp);
            }
        });

    }





    public interface HintPlayFinishListener{
        void playFinish(MediaPlayer mediaPlayer);

    }



    /**
     * 获取mediaPlay
     * @param context
     * @return
     */
    private MediaPlayer getMediaPlayer(Context context) {
        MediaPlayer mediaplayer = new MediaPlayer();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return mediaplayer;
        }
        try {
            Class<?> cMediaTimeProvider = Class.forName("android.media.MediaTimeProvider");
            Class<?> cSubtitleController = Class.forName("android.media.SubtitleController");
            Class<?> iSubtitleControllerAnchor = Class.forName("android.media.SubtitleController$Anchor");
            Class<?> iSubtitleControllerListener = Class.forName("android.media.SubtitleController$Listener");
            Constructor constructor = cSubtitleController.getConstructor(
                    new Class[]{Context.class, cMediaTimeProvider, iSubtitleControllerListener});
            Object subtitleInstance = constructor.newInstance(context, null, null);
            Field f = cSubtitleController.getDeclaredField("mHandler");
            f.setAccessible(true);
            try {
                f.set(subtitleInstance, new android.os.Handler());
            } catch (IllegalAccessException e) {
                return mediaplayer;
            } finally {
                f.setAccessible(false);
            }
            Method setsubtitleanchor = mediaplayer.getClass().getMethod("setSubtitleAnchor",
                    cSubtitleController, iSubtitleControllerAnchor);
            setsubtitleanchor.invoke(mediaplayer, subtitleInstance, null);
        } catch (Exception e) {
            Log.d(TAG,"getMediaPlayer crash ,exception = "+e);
        }
        return mediaplayer;
    }

}
