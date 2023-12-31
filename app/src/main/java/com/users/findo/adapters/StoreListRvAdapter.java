package com.users.findo.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.users.findo.activities.AllStores;
import com.users.findo.activities.StoreDetails;
import com.users.findo.dataClasses.Store;
import com.users.findo.R;

import java.util.ArrayList;
import java.util.List;

public class StoreListRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Store> mStoreList;
    Context context;

    public interface ItemClickListener{
        void itemOnClickListener(View v, int position);
    }

    ItemClickListener itemClickListener;

    public void setFilteredItemList(List<Store> storeList){
        mStoreList = storeList;
        notifyDataSetChanged();
    }

    public StoreListRvAdapter(Context context,List<Store> storeList,ItemClickListener itemClickListener){
        mStoreList = storeList;
        this.context =context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //initialise view
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //inserting data
        MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.storeName.setText(mStoreList.get(position).getStoreName());
        viewHolder.storeDist.setText( mStoreList.get(position).getDist() + " Km");

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_happy);
        Glide.with(viewHolder.storeImage.getContext()).load(mStoreList.get(position).getStoreUrl()).apply(options).into(viewHolder.storeImage);


        viewHolder.itemView.setOnClickListener(v -> {
//                itemClickListener.itemOnClickListener(v, holder.getAdapterPosition());
            Intent i = new Intent(context, StoreDetails.class);
            i.putExtra("store",mStoreList.get(position));
            context.startActivity(i);
        });

    }


    @Override
    public int getItemCount() {
        return mStoreList.size();
    }


    public  class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView storeName;
        private final TextView storeDist;
        private final ImageView storeImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            storeName = itemView.findViewById(R.id.storeName);
            storeDist = itemView.findViewById(R.id.storeDist);
            storeImage = itemView.findViewById(R.id.storeImage);
        }
    }
}
