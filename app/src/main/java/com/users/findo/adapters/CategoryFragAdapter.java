package com.users.findo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;
import com.users.findo.dataClasses.Category;
import com.users.findo.R;

import java.util.ArrayList;

public class CategoryFragAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<Category> categoryArrayList;

    public CategoryFragAdapter(ArrayList<Category> categoryArrayList){
        this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_frag_list_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //inserting data
        MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.categoryName.setText(categoryArrayList.get(position).getName());
//        viewHolder.back.setBackgroundColor(categoryArrayList.get(position).getBackColor());
//        viewHolder.back.setStrokeColor(categoryArrayList.get(position).getStrokeColor());

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
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);
            back = itemView.findViewById(R.id.backCardCategory);
        }
    }
}
