package com.users.findo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.users.findo.R;
import com.users.findo.activities.GeneralDashboard;
import com.users.findo.activities.GroceryDashboard;
import com.users.findo.dataClasses.Category;

import java.util.List;

public class GcategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Category> categoryArrayList;
    Context context;
    public GcategoryAdapter( Context context ,List<Category> categoryArrayList){
        this.context=context;
        this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grocery_category_item,parent,false);

        return new MyViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //inserting data
        MyViewHolder1 viewHolder = (MyViewHolder1) holder;
        viewHolder.categoryName.setText(categoryArrayList.get(position).getName());
        setAnimation(holder.itemView,position);
        holder.itemView.setOnClickListener(v -> {
            if(categoryArrayList.get(position).getName().equals("Grocery") || categoryArrayList.get(position).getName().equals("General Store")){
                context.startActivity(new Intent(context, GroceryDashboard.class));
            }else{
                Intent i = new Intent(context, GeneralDashboard.class);
                i.putExtra("category",categoryArrayList.get(position).getName());
                context.startActivity(i);
            }
        });
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_happy);
        Glide.with(viewHolder.categoryImage.getContext()).load(categoryArrayList.get(position).getLink()).apply(options).into(viewHolder.categoryImage);
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public static class MyViewHolder1 extends RecyclerView.ViewHolder{

        ImageView categoryImage;
        TextView categoryName;
        CardView back;
        public MyViewHolder1(@NonNull View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);
            back = itemView.findViewById(R.id.backCardCategory);
        }
    }
    private void setAnimation(View viewToAnimate , int position) {
        Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        viewToAnimate.startAnimation(fadeIn);

    }
}
