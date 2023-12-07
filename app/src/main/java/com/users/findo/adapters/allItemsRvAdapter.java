package com.users.findo.adapters;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.users.findo.R;
import com.users.findo.dataClasses.Item;
import com.users.findo.databaseClass.CartDb;
import com.users.findo.databaseClass.FavDb;

import java.util.List;

public class allItemsRvAdapter extends RecyclerView.Adapter<allItemsRvAdapter.MyViewHolder1> {


    Context context;
    List<Item> itemList;
    AllItemListener clickListener;


    public interface AllItemListener{
        void FavItemOnClick(MyViewHolder1 v, int position);
        void AddToCartItemOnClick(MyViewHolder1 v, int position);
        void RemoveFromCartItemOnClick(MyViewHolder1 v, int position);

        void ItemOnClick(MyViewHolder1 v, int position);
    }

    public allItemsRvAdapter(Context context, List<Item> itemList, AllItemListener clickListener){
        this.context = context;
        this.itemList = itemList;
        this.clickListener = clickListener;
    }



        @Override
        public int getItemCount() {
            return itemList.size();
        }




    @NonNull
    @Override
    public allItemsRvAdapter.MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View menuItemView = LayoutInflater.from(context).inflate(R.layout.item_item_list, parent, false);
            return new MyViewHolder1(menuItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull allItemsRvAdapter.MyViewHolder1 holder, int position) {
        holder.ItemName.setText(itemList.get(holder.getAdapterPosition()).getItemName());
        holder.desc.setText(itemList.get(holder.getAdapterPosition()).getItemDescription());
        holder.price.setText(itemList.get(holder.getAdapterPosition()).getPrice() + " Rs");

        // Get a reference to the system vibrator service
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // Define the duration for which to vibrate the phone (in milliseconds)
        int duration = 100;

        CartDb cartDb = new CartDb(context);
        if (cartDb.itemExist(itemList.get(holder.getAdapterPosition()).getItemId())) {
            holder.addToCart.setVisibility(View.GONE);
            holder.removeFromCart.setVisibility(View.VISIBLE);
        } else {
            holder.addToCart.setVisibility(View.VISIBLE);
            holder.removeFromCart.setVisibility(View.GONE);
        }

        FavDb favDb = new FavDb(context);
        if (favDb.itemExist(itemList.get(holder.getAdapterPosition()).getItemId())) {
            holder.favImage.setImageResource(R.drawable.ic_redheart);
        } else {
            holder.favImage.setImageResource(R.drawable.ic_white_heart);
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_happy);
        Glide.with(holder.ImageUrl.getContext()).load(itemList.get(holder.getAdapterPosition()).getItemUrl()).apply(options).into(((MyViewHolder1) holder).ImageUrl);

        holder.addToCart.setOnClickListener(v -> {
            clickListener.AddToCartItemOnClick(holder, holder.getAdapterPosition());
            vibrator.vibrate(duration);
        });

        ( holder).removeFromCart.setOnClickListener(view -> {
            clickListener.RemoveFromCartItemOnClick(holder, holder.getAdapterPosition());
            vibrator.vibrate(duration);
        });

        holder.favImage.setOnClickListener(view -> {
            clickListener.FavItemOnClick( holder, holder.getAdapterPosition());
            vibrator.vibrate(duration);
        });

        ( holder).itemView.setOnClickListener(v -> {
            clickListener.ItemOnClick( holder, holder.getAdapterPosition());

        });
    }

    public static class MyViewHolder1 extends RecyclerView.ViewHolder{


            TextView ItemName;
            ImageView ImageUrl;
            public TextView addToCart,desc;
            public TextView removeFromCart,price;
            public LottieAnimationView heart  ;
            public  LottieAnimationView sparkle  ;

            public ImageView favImage;

            public MyViewHolder1(@NonNull View itemView) {
                super(itemView);

                ItemName = itemView.findViewById(R.id.item_name);
                desc = itemView.findViewById(R.id.item_short_desc);
                ImageUrl = itemView.findViewById(R.id.item_img);
                addToCart = itemView.findViewById(R.id.addToCart);
                removeFromCart = itemView.findViewById(R.id.removeFromCart);
                favImage = itemView.findViewById(R.id.fav_img);
                heart = itemView.findViewById(R.id.favLottie);
                sparkle = itemView.findViewById(R.id.sparkleLottie);
                price = itemView.findViewById(R.id.item_price);

            }
        }
}

