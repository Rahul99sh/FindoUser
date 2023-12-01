package com.users.findo.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.users.findo.dataClasses.CartDatabase;
import com.users.findo.R;
import com.users.findo.dataClasses.Item;

import java.util.ArrayList;
import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> cartArrayList;
    private Context context;


    public interface RemoveItemClicked{
        void removeItem(int position);
    }

    RemoveItemClicked removeItemClicked;
    public CartListAdapter( Context context,List<Item> cartArrayList,RemoveItemClicked removeItemClicked) {
        this.context = context;
        this.cartArrayList = cartArrayList;
        this.removeItemClicked = removeItemClicked;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_details,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.cartListText.setText(cartArrayList.get(holder.getAdapterPosition()).getItemName());
        viewHolder.storeName.setText(cartArrayList.get(holder.getAdapterPosition()).getStoreName());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_happy);

        Glide.with(viewHolder.cartListImage.getContext()).load(cartArrayList.get(holder.getAdapterPosition()).getItemUrl()).apply(options).into(viewHolder.cartListImage);

        viewHolder.cartListDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeItemClicked.removeItem(holder.getAdapterPosition());
                notifyDataSetChanged();

            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a URI for the coordinates
                String uri = "google.navigation:q=" + cartArrayList.get(position).getStoreLat() + "," + cartArrayList.get(position).getStoreLong();

                // Create an intent to open the Google Maps app with the URI
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps"); // Specify package to ensure Maps app is launched

                // Start the intent
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView cartListImage;
        TextView cartListText;
        ImageView cartListDelete;

        TextView storeName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cartListImage = itemView.findViewById(R.id.cartListImage);
            cartListText = itemView.findViewById(R.id.cartListText);
            cartListDelete = itemView.findViewById(R.id. cartListDelete);
            storeName = itemView.findViewById(R.id.storeName);
        }
    }
}
