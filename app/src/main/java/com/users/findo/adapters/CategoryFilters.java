package com.users.findo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.users.findo.R;
import com.users.findo.dataClasses.Category;

import java.util.List;

public class CategoryFilters extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Category> categoryArrayList;
    public interface itemClick{
        public void onItemClick(View v,int pos,int selpos);
    }
    Category sele = null;
    int selpos = -1;
    itemClick itemClick;
    Context context;
    public CategoryFilters( Context context ,List<Category> categoryArrayList, itemClick itemClick){
        this.context=context;
        this.categoryArrayList = categoryArrayList;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryfilter,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //inserting data
        MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.categoryName.setText(categoryArrayList.get(position).getName());
        viewHolder.itemView.setOnClickListener(v -> {
            itemClick.onItemClick(viewHolder.backg,position,selpos);
            if (selpos == -1) {
                selpos = position;
                sele = categoryArrayList.get(position);
            } else {
                if (selpos != position) {
                    // When a different item is clicked
                    selpos = position;
                    sele = categoryArrayList.get(position);
                } else {
                    selpos = -1; // Reset selection
                    sele = null; // Reset selection data if needed
                }
            }
        });

        setAnimation(holder.itemView,position);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_happy);
        Glide.with(viewHolder.categoryImage.getContext()).load(categoryArrayList.get(position).getLink()).apply(options).into(viewHolder.categoryImage);
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView categoryImage;
        TextView categoryName;
        CardView back;
        LinearLayout backg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);
            backg = itemView.findViewById(R.id.back);
            back = itemView.findViewById(R.id.backCardCategory);
        }
    }
    private void setAnimation(View viewToAnimate , int position) {
        Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        viewToAnimate.startAnimation(fadeIn);

    }
}
