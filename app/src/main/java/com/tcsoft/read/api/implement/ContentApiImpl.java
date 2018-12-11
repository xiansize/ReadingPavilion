package com.tcsoft.read.api.implement;

import android.util.Log;

import com.tcsoft.read.api.interfaces.ContentApi;
import com.tcsoft.read.netword.NetwordHelper;
import com.tcsoft.read.utils.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiansize on 2017/11/27.
 */
public class ContentApiImpl implements ContentApi{


    @Override
    public String getCategory() {
        Map<String,String> map = new HashMap<>();
        map.put("token",Constant.URL_TOKEN);
        map.put("id",Constant.ACTIVITY_ID);
        return NetwordHelper.sendDataByPost(map,Constant.URL_READING+"/webserver/activity/show");
    }




    @Override
    public String getContentList(String page,String id,String title) {
        if(title == null){
            Map<String,String> map = new HashMap<>();
            map.put("token",Constant.URL_TOKEN);
            map.put("articleTypeId",id);
            map.put("state","2");
            map.put("pageNum",page);//第几页
            map.put("pageSize","6");
            map.put("activityId",Constant.ACTIVITY_ID);
            return  NetwordHelper.sendDataByPost(map,Constant.URL_READING+"/webserver/article/findPage");
        }
        Map<String,String> map = new HashMap<>();
        map.put("token",Constant.URL_TOKEN);
        map.put("titleName",title);
        map.put("author",title);
        map.put("state","2");
        map.put("pageNum",page);//第几页
        map.put("pageSize","6");
        map.put("activityId",Constant.ACTIVITY_ID);
        return  NetwordHelper.sendDataByPost(map,Constant.URL_READING+"/webserver/article/findPage");

    }

    @Override
    public String getContentShow(String articleId) {
        return  NetwordHelper.getDataByGet(Constant.URL_READING+"/webserver/article/show?articleId="+articleId+"&token="+Constant.URL_TOKEN);
    }

    @Override
    public String getBackground() {
//        Map map = new HashMap<String,Object>();
//        map.put("token",Constant.URL_TOKEN);
//        map.put("libraryId",Constant.LIB_CODE);
        return NetwordHelper.getDataByGet(Constant.URL_READING+"/webserver/music/findAll?token="+Constant.URL_TOKEN+"&libraryId="+Constant.LIB_CODE);
    }

    @Override
    public String getActivity() {
        Map<String,String> map = new HashMap<>();
        map.put("token",Constant.URL_TOKEN);
        map.put("pageNum","1");
        map.put("pageSize","99");
        map.put("libraryId",Constant.LIB_CODE);
        return NetwordHelper.sendDataByPost(map,Constant.URL_READING+"/webserver/activity/list");
    }


}
