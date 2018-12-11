package com.tcsoft.read.action.interfaces;

import com.tcsoft.read.callback.ActionListener;
import com.tcsoft.read.entity.Activity;
import com.tcsoft.read.entity.Art;
import com.tcsoft.read.entity.Background;
import com.tcsoft.read.entity.Type;

import java.util.List;

/**
 * Created by xiansize on 2017/11/27.
 */
public interface ContentAction {

    void getCategory(ActionListener<List<Type>> listener);

    void getReadingContent(String title,ActionListener<Art> listener);

    void getContentList(String page,String id,String title,ActionListener<List<Art>> listener);

    void getBackground(ActionListener<List<Background>> listener);

    void getActivity(ActionListener<List<Activity>> listener);



}
