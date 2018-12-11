package com.tcsoft.read.callback;

/**
 * Created by xiansize on 2017/11/21.
 */
public interface ActionListener<T>{


    void onSucceed(T t);

    void onFailed(String message);



}
