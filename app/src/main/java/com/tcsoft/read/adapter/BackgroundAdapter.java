package com.tcsoft.read.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tcsoft.read.R;
import com.tcsoft.read.entity.Background;

import java.util.List;

/**
 * Created by Administrator on 2018/3/19 0019.
 */

public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.BackgroundViewHolder> implements View.OnClickListener{

    private List<Background> mList;
    private LayoutInflater layoutInflater;
    private BackgroundItemListener backgroundItemListener;


    public BackgroundAdapter(List<Background> mList, Context context) {
        this.mList = mList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setBackgroundItemListener(BackgroundItemListener backgroundItemListener) {
        this.backgroundItemListener = backgroundItemListener;
    }

    @Override
    public BackgroundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_item_background, parent, false);
        BackgroundViewHolder backgroundViewHolder = new BackgroundViewHolder(view);
        view.setOnClickListener(this);
        return backgroundViewHolder;
    }

    @Override
    public void onBindViewHolder(BackgroundViewHolder holder, int position) {
        holder.tvType.setText(mList.get(position).getMusicType());

        //无背景音乐
        if(position == 0){
            holder.rlBackGround.setBackgroundResource(R.drawable.shape_code_grey);


        }else if(position == 1){
            //默认背景音乐
            holder.rlBackGround.setBackgroundResource(R.drawable.shape_code_grey);

        }else {
            switch ((position + 1) % 4) {
                case 1:
                    holder.rlBackGround.setBackgroundResource(R.drawable.shape_code_purple);
                    break;
                case 2:
                    holder.rlBackGround.setBackgroundResource(R.drawable.shape_code_login_button);
                    break;
                case 3:
                    holder.rlBackGround.setBackgroundResource(R.drawable.shape_qr_code_login_button);
                    break;
                case 0:
                    holder.rlBackGround.setBackgroundResource(R.drawable.shape_code_red);
                    break;
            }

        }

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        if(backgroundItemListener != null && v.getTag() != null)
            backgroundItemListener.onClickItemListener(v, (Integer) v.getTag());
    }


    public class BackgroundViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rlBackGround;
        private TextView tvType;

        public BackgroundViewHolder(View view) {
            super(view);
            rlBackGround = (RelativeLayout) view.findViewById(R.id.rl_background_item_background);
            tvType = (TextView) view.findViewById(R.id.tv_background_item_title_name);
        }
    }


    public interface BackgroundItemListener {

        void onClickItemListener(View view, int position);

    }

}
