package com.tcsoft.read.thread;

/**
 * Created by xiansize on 2017/8/18.
 */
public interface AsyncTaskListener<T> {


    String background();


    void result(T t);


}
