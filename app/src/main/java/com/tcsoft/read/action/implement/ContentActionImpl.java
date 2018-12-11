package com.tcsoft.read.action.implement;

import android.util.Log;

import com.tcsoft.read.action.interfaces.ContentAction;
import com.tcsoft.read.api.implement.ContentApiImpl;
import com.tcsoft.read.api.interfaces.ContentApi;
import com.tcsoft.read.callback.ActionListener;
import com.tcsoft.read.entity.Activity;
import com.tcsoft.read.entity.Art;
import com.tcsoft.read.entity.Background;
import com.tcsoft.read.entity.Type;
import com.tcsoft.read.thread.AsyncTaskAction;
import com.tcsoft.read.thread.AsyncTaskListener;
import com.tcsoft.read.utils.Constant;
import com.tcsoft.read.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiansize on 2017/11/27.
 */
public class ContentActionImpl implements ContentAction {

    private static final String TAG = "ContentActionImpl";

    private AsyncTaskAction asyncTaskAction;
    private ContentApi contentApi;

    public ContentActionImpl() {
        this.contentApi = new ContentApiImpl();
    }

    @Override
    public void getCategory(final ActionListener<List<Type>> listener) {
        asyncTaskAction = new AsyncTaskAction(new AsyncTaskListener<String>() {
            @Override
            public String background() {
                return contentApi.getCategory();
            }

            @Override
            public void result(String result) {
                if (result != null) {
                    Log.d(TAG,result.toString());

                    try {
                        JSONObject json = new JSONObject(result);
                        List<Type> list = new ArrayList<>();
                        JSONArray jsonArray = json.getJSONArray("articleTypes");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            Type type = new Type();
                            type.setTypeId(jsonArray.getJSONObject(i).getInt("typeId"));
                            type.setTypeName(jsonArray.getJSONObject(i).getString("typeName"));
                            list.add(type);
                        }
                        listener.onSucceed(list);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "数据异常");

                    }
                } else {
                    listener.onFailed("连接服务器失败");
                }

            }
        });
        asyncTaskAction.execute();
    }


    @Override
    public void getReadingContent(final String title, final ActionListener<Art> listener) {
        asyncTaskAction = new AsyncTaskAction(new AsyncTaskListener<String>() {
            @Override
            public String background() {
                return contentApi.getContentShow(title);
            }

            @Override
            public void result(String result) {
                if (result != null) {

                    Log.d(TAG, result.toString());

                    JSONObject json = null;
                    Art art = new Art();

                    try {

                        json = new JSONObject(result);
                        art.setTitle(json.getString("titleName"));
                        art.setContent(json.getString("articletext"));

                    } catch (JSONException e) {
                        listener.onFailed("连接服务器异常");
                        e.printStackTrace();
                    }

//                    try {
//                        json = json.getJSONObject("music");
//                        art.setMusic(json.getString("musicPath"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                    listener.onSucceed(art);


                } else {

                    listener.onFailed("连接服务器失败");


                }


            }
        });
        asyncTaskAction.execute();
    }


    @Override
    public void getContentList(final String page, final String id, final String title, final ActionListener<List<Art>> listener) {
        asyncTaskAction = new AsyncTaskAction(new AsyncTaskListener<String>() {
            @Override
            public String background() {
                return contentApi.getContentList(page,id, title);
            }

            @Override
            public void result(String result) {

                if (result != null) {

                    Log.d(TAG, result.toString());

                    try {
                        JSONObject json = new JSONObject(result);
                        List<Art> list = new ArrayList<>();
                        JSONArray jsonArray = json.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Art art = new Art();
                            art.setTitle(jsonArray.getJSONObject(i).getString("titleName"));
                            art.setAuthor(jsonArray.getJSONObject(i).getString("author"));
//                            art.setType(jsonArray.getJSONObject(i).getJSONObject("articleType").getString("articleTypeName"));
                            art.setTotalTime(jsonArray.getJSONObject(i).getString("updateTime"));
                            art.setContent(jsonArray.getJSONObject(i).getString("updateTime"));
                            art.setArtId(jsonArray.getJSONObject(i).getString("id"));
                            art.setMusic(jsonArray.getJSONObject(i).getString("bMusicId"));
                            art.settPage(json.getInt("pages"));
                            art.setcPage(json.getInt("pageNum"));
                            list.add(art);
                        }


                        listener.onSucceed(list);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "数据异常");

                    }
                } else {
                    listener.onFailed("连接服务器失败");
                }

            }
        });
        asyncTaskAction.execute();
    }

    @Override
    public void getBackground(final ActionListener<List<Background>> listener) {
        asyncTaskAction = new AsyncTaskAction(new AsyncTaskListener<String>() {
            @Override
            public String background() {
                return contentApi.getBackground();
            }

            @Override
            public void result(String result) {
                if (result != null) {

                    Log.d(TAG,result.toString());

                    try {
                        JSONArray json = new JSONArray(result);
                        List<Background> list = new ArrayList<>();

                        //无背景音乐
                        Background noBackground = new Background();
                        noBackground.setMusicType("无音乐");
                        list.add(noBackground);

                        //默认背景音乐
                        Background defaultBackground = new Background();
                        defaultBackground.setMusicType("默认音乐");
                        defaultBackground.setMusicpPath(json.getJSONObject(0).getString("musicPath"));
                        list.add(defaultBackground);

                        if(json.length() == 0){
                            listener.onFailed("暂无背景音乐");

                        }else{

                            for (int i = 0; i < json.length(); i++) {
                                Background background = new Background();
                                background.setMusicName(json.getJSONObject(i).getString("musicName"));
                                background.setMusicType(json.getJSONObject(i).getString("musicType"));
                                background.setMusicpPath(json.getJSONObject(i).getString("musicPath"));
                                list.add(background);
                            }
                            listener.onSucceed(list);

                        }




                    } catch (JSONException e) {
                        listener.onFailed("暂无背景音乐");
                    }

                } else {

                    listener.onFailed("连接服务器失败");
                }

            }
        });
        asyncTaskAction.execute();
    }



    @Override
    public void getActivity(final ActionListener<List<Activity>> listener) {
        asyncTaskAction = new AsyncTaskAction(new AsyncTaskListener<String>() {
            @Override
            public String background() {
                return contentApi.getActivity();
            }

            @Override
            public void result(String result) {
                if(result != null){

                    Log.d(TAG,result.toString());

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        List<Activity> list = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++){
                            Activity activity = new Activity();
                            activity.setaId(jsonArray.getJSONObject(i).getString("id"));
                            activity.setaName(jsonArray.getJSONObject(i).getString("name"));
                            activity.setaPoster(jsonArray.getJSONObject(i).getString("img"));
                            list.add(activity);
                        }



                        listener.onSucceed(list);


                    } catch (JSONException e) {
                        listener.onFailed("连接服务器异常");
                    }

                }else {
                    listener.onFailed("连接服务器失败");
                }

            }
        });
        asyncTaskAction.execute();

    }


}
