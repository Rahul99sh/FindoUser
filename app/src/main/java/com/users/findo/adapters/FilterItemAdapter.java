package com.users.findo.adapters;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.users.findo.dataClasses.CartDatabase;
import com.users.findo.databaseClass.CartDb;
import com.users.findo.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FilterItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<CartDatabase> filteredItemList;
    Context context;

    public interface ItemClickListener{
        void AddToCartItemOnClick(MyNewViewHolder v, int position);
        void RemoveFromCartItemOnClick(MyNewViewHolder v, int position);

        void ItemOnClick(MyNewViewHolder v, int position);
    }

    public void setFilteredItemList(ArrayList<CartDatabase> storeList){
        filteredItemList = storeList;
        notifyDataSetChanged();
    }

    ItemClickListener itemClickListener;

    public FilterItemAdapter(Context context, ArrayList<CartDatabase> storeList, ItemClickListener itemClickListener){
        filteredItemList = storeList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //initialise view
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_filter_item_items_adapter,parent,false);

        return new MyNewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //inserting data
        MyNewViewHolder viewHolder = (MyNewViewHolder) holder;
        CartDb cartDb = new CartDb(context);
        if(cartDb.itemExist(filteredItemList.get(holder.getAdapterPosition()).getItemId())){
            viewHolder.addToCart.setVisibility(View.GONE);
            viewHolder.removeFromCart.setVisibility(View.VISIBLE);
        }else{
            viewHolder.addToCart.setVisibility(View.VISIBLE);
            viewHolder.removeFromCart.setVisibility(View.GONE);
        }


        viewHolder.itemName.setText(filteredItemList.get(holder.getAdapterPosition()).getItemName());
        viewHolder.price.setText(filteredItemList.get(holder.getAdapterPosition()).getPrice());
        viewHolder.storeName.setText( filteredItemList.get(holder.getAdapterPosition()).getStoreName());
        if(filteredItemList.get(holder.getAdapterPosition()).getItemTag().equals("")){
            viewHolder.tag.setVisibility(View.INVISIBLE);
        } else if (filteredItemList.get(holder.getAdapterPosition()).getItemTag().equals("New")) {
            viewHolder.tagText.setText("New");
        }else {
            viewHolder.tagText.setText("Featured");
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_happy);
        Glide.with(viewHolder.itemName.getContext()).load(filteredItemList.get(holder.getAdapterPosition()).getItemUrl()).apply(options).into(viewHolder.itemUrl);

        // Get a reference to the system vibrator service
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // Define the duration for which to vibrate the phone (in milliseconds)
        int duration = 100;

        viewHolder.addToCart.setOnClickListener(v -> {
            itemClickListener.AddToCartItemOnClick(viewHolder,holder.getAdapterPosition());
            vibrator.vibrate(duration);
        });

        viewHolder.removeFromCart.setOnClickListener(view -> {
            itemClickListener.RemoveFromCartItemOnClick(viewHolder,holder.getAdapterPosition());
            vibrator.vibrate(duration);
        });

        viewHolder.itemView.setOnClickListener(v -> {
            itemClickListener.ItemOnClick(viewHolder,holder.getAdapterPosition());
        });

    }


    @Override
    public int getItemCount() {
        return filteredItemList.size();
    }


    public static class MyNewViewHolder extends RecyclerView.ViewHolder{

        private TextView itemName;
        private TextView storeName;
        public TextView addToCart;
        public TextView removeFromCart;
        private TextView tagText, price;
        private LinearLayout tag;
        private CircleImageView itemUrl;
        public MyNewViewHolder(@NonNull View itemView) {
            super(itemView);


            itemName = itemView.findViewById(R.id.item_name);
            price = itemView.findViewById(R.id.item_price);
            storeName = itemView.findViewById(R.id.item_store);
            itemUrl = itemView.findViewById(R.id.item_img);
            addToCart = itemView.findViewById(R.id.addToCart);
            removeFromCart = itemView.findViewById(R.id.removeFromCart);
            tag = itemView.findViewById(R.id.tagLayout);
            tagText = itemView.findViewById(R.id.tag);
        }
    }
}
