package com.users.findo.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.users.findo.dataClasses.Store;
import com.users.findo.R;

import java.util.ArrayList;

public class StoreListRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<Store> mStoreList;
    Context context;

    public interface ItemClickListener{
        void itemOnClickListener(View v, int position);
    }

    ItemClickListener itemClickListener;

    public void setFilteredItemList(ArrayList<Store> storeList){
        mStoreList = storeList;
        notifyDataSetChanged();
    }

    public StoreListRvAdapter(Context context,ArrayList<Store> storeList,ItemClickListener itemClickListener){
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
        viewHolder.storeDist.setText( mStoreList.get(position).getDistance().toString());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_happy);
        Glide.with(viewHolder.storeImage.getContext()).load(mStoreList.get(position).getStoreUrl()).apply(options).into(viewHolder.storeImage);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemOnClickListener(v, holder.getAdapterPosition());

            }
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
