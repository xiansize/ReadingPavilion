package com.tcsoft.read.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tcsoft.read.R;
import com.tcsoft.read.entity.Type;

import java.util.List;

/**
 * Created by xiansize on 2017/11/27.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> implements View.OnClickListener{

    private List<Type> mList;
    private LayoutInflater layoutInflater;
    private CategoryItemListener categoryItemListener;



    public CategoryAdapter(List<Type> mList, Context context) {
        this.mList = mList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setCategoryItemListener(CategoryItemListener categoryItemListener) {
        this.categoryItemListener = categoryItemListener;
    }


    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_category_item,parent,false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(view);
        view.setOnClickListener(this);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.tvCategory.setText(mList.get(position).getTypeName());

        switch ((position+1) % 4){
            case 1: holder.rlBackGround.setBackgroundResource(R.drawable.shape_code_purple);
                break;
            case 2: holder.rlBackGround.setBackgroundResource(R.drawable.shape_code_login_button);
                break;
            case 3: holder.rlBackGround.setBackgroundResource(R.drawable.shape_qr_code_login_button);
                break;
            case 0: holder.rlBackGround.setBackgroundResource(R.drawable.shape_code_red);
                break;
        }

        holder.itemView.setTag(position);

    }



    @Override
    public int getItemCount() {
        return mList.size();
    }


    @Override
    public void onClick(View v) {
        if(categoryItemListener != null && v.getTag() != null)
            categoryItemListener.onClickItemListener(v, (Integer) v.getTag());

    }


    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout rlBackGround;
        private TextView tvCategory;

        public CategoryViewHolder(View view) {
            super(view);
            rlBackGround = (RelativeLayout) view.findViewById(R.id.rl_category_item_background);
            tvCategory = (TextView) view.findViewById(R.id.tv_category_item_title_name);
        }
    }






    public interface CategoryItemListener{

        void onClickItemListener(View view,int position);

    }


}
