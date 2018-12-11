package com.tcsoft.read.api.interfaces;

/**
 * Created by xiansize on 2017/11/27.
 */
public interface ContentApi {

    String getCategory();

    String getContentList(String page,String id, String title);

    String getContentShow(String articleId);

    String getBackground();

    String getActivity();

}
