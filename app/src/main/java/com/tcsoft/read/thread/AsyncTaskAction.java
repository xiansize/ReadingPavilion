package com.tcsoft.read.thread;

import android.os.AsyncTask;



/**
 * Created by xiansize on 2017/8/18.
 */
public class AsyncTaskAction extends AsyncTask<Void,Void,String>{


    private AsyncTaskListener<String> asyncTaskListener;


    public AsyncTaskAction(AsyncTaskListener<String> asyncTaskListener) {
        this.asyncTaskListener = asyncTaskListener;
    }


    @Override
    protected String doInBackground(Void... params) {
        return asyncTaskListener.background();
    }



    @Override
    protected void onPostExecute(String result) {
        asyncTaskListener.result(result);
    }




}
