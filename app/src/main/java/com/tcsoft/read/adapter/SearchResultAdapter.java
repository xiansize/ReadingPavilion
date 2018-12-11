package com.tcsoft.read.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tcsoft.read.R;
import com.tcsoft.read.entity.Art;
import com.tcsoft.read.utils.Constant;
import com.tcsoft.read.utils.TimeUtil;

import java.util.List;

/**
 * Created by xiansize on 2017/11/27.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> implements View.OnClickListener{

    private List<Art> mList;
    private LayoutInflater layoutInflater;
    private SearchResultItemListener searchResultItemListener;

    public void setSearchResultItemListener(SearchResultItemListener searchResultItemListener) {
        this.searchResultItemListener = searchResultItemListener;
    }

    public SearchResultAdapter(List<Art> mList, Context context) {
        this.mList = mList;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_result_list,parent,false);
        SearchResultViewHolder searchResultViewHolder = new SearchResultViewHolder(view);
        view.setOnClickListener(this);
        return searchResultViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {

//        if(Constant.VALUE_NULL.equals(mList.get(position).getTitle()) && Constant.VALUE_NULL.equals(mList.get(position).getArtId())){
//            holder.rlResult.setVisibility(View.INVISIBLE);
//
//        }
            holder.tvTitle.setText(mList.get(position).getTitle());
            holder.tvAuthor.setText("作  者: "+mList.get(position).getAuthor());
            //holder.tvType.setText("类 型: "+mList.get(position).getType());
            holder.tvTotalTime.setText("上传时间: "+ TimeUtil.getFormatTime(mList.get(position).getTotalTime()));

        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    @Override
    public void onClick(View v) {
        if(v.getTag() != null && searchResultItemListener != null) {
            searchResultItemListener.SearchResultItemListener(v, (Integer) v.getTag());
        }
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvType;
        private RelativeLayout rlResult;
        private TextView tvTotalTime;

        public SearchResultViewHolder(View view) {
            super(view);
            rlResult = (RelativeLayout) view.findViewById(R.id.rl_item_search_result);
            tvTitle = (TextView) view.findViewById(R.id.tv_title_item_search_result);
            tvAuthor = (TextView) view.findViewById(R.id.tv_author_item_search_result);
            tvType = (TextView) view.findViewById(R.id.tv_type_item_search_result);
            tvTotalTime = (TextView) view.findViewById(R.id.tv_total_time_item_search_result);


        }
    }





    public interface SearchResultItemListener{
        void SearchResultItemListener(View view,int position);

    }


}
