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
import com.users.findo.dataClasses.CartDatabase;
import com.users.findo.databaseClass.CartDb;
import com.users.findo.databaseClass.FavDb;
import com.users.findo.R;

import java.util.ArrayList;

public class ItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<CartDatabase> itemList ;
    ItemClickListener clickListener;

    public interface ItemClickListener{
        void FavItemOnClick(MyViewHolder v, int position);
        void AddToCartItemOnClick(MyViewHolder v, int position);
        void RemoveFromCartItemOnClick(MyViewHolder v, int position);

        void ItemOnClick(MyViewHolder v, int position);
    }

    public ItemListAdapter(Context context,ArrayList<CartDatabase> itemList,ItemClickListener clickListener){
        this.context = context;
        this.itemList = itemList;
        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View menuItemView = LayoutInflater.from(context).inflate(R.layout.item_item_list, parent, false);
        return new MyViewHolder(menuItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.ItemName.setText(itemList.get(holder.getAdapterPosition()).getItemName());
        myViewHolder.price.setText(itemList.get(holder.getAdapterPosition()).getPrice() + " Rs");

        // Get a reference to the system vibrator service
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // Define the duration for which to vibrate the phone (in milliseconds)
        int duration = 100;

//            if (itemList.get(position).getItemTag().equals("")) {
//                myViewHolder.tagText.setVisibility(View.INVISIBLE);}
//            } else if (itemList.get(position).getItemTag().equals("New")) {
//                myViewHolder.tagText.setText("New");
//            } else {
//                myViewHolder.tagText.setText("Featured");
//                myViewHolder.tagText.setBackgroundColor(context.getColor(R.color.featured));
//                myViewHolder.tagText.setTextColor(context.getColor(R.color.white));
//            }

        CartDb cartDb = new CartDb(context);
        if (cartDb.itemExist(itemList.get(holder.getAdapterPosition()).getItemId())) {
            myViewHolder.addToCart.setVisibility(View.GONE);
            myViewHolder.removeFromCart.setVisibility(View.VISIBLE);
        }

        FavDb favDb = new FavDb(context);
        if (favDb.itemExist(itemList.get(holder.getAdapterPosition()).getItemId())) {
            myViewHolder.favImage.setImageResource(R.drawable.ic_redheart);
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_happy);
        Glide.with(myViewHolder.ImageUrl.getContext()).load(itemList.get(holder.getAdapterPosition()).getItemUrl()).apply(options).into(myViewHolder.ImageUrl);


        myViewHolder.addToCart.setOnClickListener(v -> {
            clickListener.AddToCartItemOnClick(myViewHolder, holder.getAdapterPosition());
            vibrator.vibrate(duration);
        });

        myViewHolder.removeFromCart.setOnClickListener(view -> {
            clickListener.RemoveFromCartItemOnClick(myViewHolder, holder.getAdapterPosition());
            vibrator.vibrate(duration);
        });

        myViewHolder.favImage.setOnClickListener(view -> {
            clickListener.FavItemOnClick(myViewHolder, holder.getAdapterPosition());
            vibrator.vibrate(duration);
        });

        myViewHolder.itemView.setOnClickListener(v -> {
            clickListener.ItemOnClick(myViewHolder, holder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        TextView ItemName;
        ImageView ImageUrl;
        public TextView addToCart;
        public TextView removeFromCart;
        public  LottieAnimationView heart  ;
        public  LottieAnimationView sparkle  ;
        private TextView tagText, price;
        public ImageView favImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ItemName = itemView.findViewById(R.id.item_name);
            ImageUrl = itemView.findViewById(R.id.item_img);
            addToCart = itemView.findViewById(R.id.addToCart);
            removeFromCart = itemView.findViewById(R.id.removeFromCart);
            favImage = itemView.findViewById(R.id.fav_img);
            heart = itemView.findViewById(R.id.favLottie);
            sparkle = itemView.findViewById(R.id.sparkleLottie);
//            tagText = itemView.findViewById(R.id.tagText);
            price = itemView.findViewById(R.id.item_price);
        }
    }
}
