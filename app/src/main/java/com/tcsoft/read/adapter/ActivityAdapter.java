package com.tcsoft.read.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tcsoft.read.R;
import com.tcsoft.read.entity.Activity;
import com.tcsoft.read.utils.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Administrator on 2018/4/16 0016.
 */

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> implements View.OnClickListener{

    private List<Activity> mList;
    private LayoutInflater layoutInflater;
    private ActivityItemListener activityItemListener;
    private Context context;


    public ActivityAdapter(List<Activity> mList, Context context) {
        this.mList = mList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

    }


    public void setActivityItemListener(ActivityItemListener activityItemListener) {
        this.activityItemListener = activityItemListener;
    }



    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_item_activity,parent,false);
        ActivityViewHolder activityViewHolder = new ActivityViewHolder(view);
        view.setOnClickListener(this);
        return activityViewHolder;
    }

    @Override
    public void onBindViewHolder(final ActivityViewHolder holder, final int position) {

        Picasso.with(context)
                .load(Constant.IMG_PATH+mList.get(position).getaPoster())
                .resize(1370,710)
                .centerCrop()
                .into(holder.ivActivity);
        holder.itemView.setTag(position);

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }



    @Override
    public void onClick(View v) {
        if(activityItemListener != null && v.getTag() != null)
            activityItemListener.onClickItemListener(v, (Integer) v.getTag());

    }


    public class ActivityViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivActivity;

        public ActivityViewHolder(View view) {
            super(view);
            ivActivity = (ImageView) view.findViewById(R.id.iv_activity_list_item);
        }
    }


    public interface ActivityItemListener{
        void onClickItemListener(View view,int position);
    }



    public Bitmap getImageBitmap(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
