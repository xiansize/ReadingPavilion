package com.tcsoft.read.action.interfaces;

import com.tcsoft.read.callback.ActionListener;

import java.util.Map;

/**
 * Created by xiansize on 2017/11/22.
 */
public interface UploadAction {


    void uploadFile(String path, Map<String,Object> paramsMap, ActionListener<String> listener);


}
