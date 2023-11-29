package com.users.findo.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.users.findo.R;
import com.users.findo.adapters.ItemListAdapter;
import com.users.findo.dataClasses.CartDatabase;
import com.users.findo.databaseClass.CartDb;
import com.users.findo.databaseClass.FavDb;

import java.util.ArrayList;

public class StoreDetails extends AppCompatActivity{

    TextView storename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_details);

        String storeId = getIntent().getExtras().getString("storeId");

        ArrayList<CartDatabase> currItem = new ArrayList<>();
//        for(CartDatabase cartDatabase : cart){
//            if(cartDatabase.getStoreId().equals(storeId)){
//                currItem.add(cartDatabase);
//            }
//        }

        storename = findViewById(R.id.storeName);
        storename.setText(currItem.get(0).getStoreName());

        RecyclerView recyclerView = findViewById(R.id.itemsRV);
        ItemListAdapter adapter = new ItemListAdapter(this, currItem, new ItemListAdapter.ItemClickListener() {
            @Override
            public void FavItemOnClick(ItemListAdapter.MyViewHolder v, int position) {
                FavDb favDb = new FavDb(StoreDetails.this);
                if(favDb.itemExist(currItem.get(position).getItemId())){
                    //item is already in fav list -> remove it
                    favDb.deleteItem(currItem.get(position).getItemId());
                    v.favImage.setImageResource(R.drawable.ic_white_heart);
                    //notify data set changed
                }else{
                    //add into favDb
                    favDb.insertOneItem(new CartDatabase(currItem.get(position).getStoreId(), currItem.get(position).getItemId()
                            , currItem.get(position).getItemName(), currItem.get(position).getStoreName(), currItem.get(position).getStoreUrl(), currItem.get(position).getItemUrl()
                            , currItem.get(position).getStoreLat(), currItem.get(position).getStoreLong(),currItem.get(position).getItemCategory(),currItem.get(position).getItemTag(),currItem.get(position).getPrice()));
                    v.favImage.setVisibility(View.GONE);
                    v.heart.setAnimation(R.raw.heart);
                    v.heart.setScaleX(1.45f);
                    v.heart.setScaleY(1.45f);
                    v.heart.setSpeed(2.5f);
                    v.heart.playAnimation();
                }
            }

            @Override
            public void AddToCartItemOnClick(ItemListAdapter.MyViewHolder v, int position) {
//                Items items= itemList.get(position);
                CartDb cartDb = new CartDb(StoreDetails.this);
                cartDb.insertOneItem(currItem.get(position));
                v.sparkle.setAnimation(R.raw.sparkle);
                v.sparkle.setScaleX(3f);
                v.sparkle.setScaleY(3f);
                v.sparkle.playAnimation();
                v.addToCart.setVisibility(View.GONE);
                v.removeFromCart.setVisibility(View.VISIBLE);
            }

            @Override
            public void RemoveFromCartItemOnClick(ItemListAdapter.MyViewHolder v, int position) {
//                Items items= itemList.get(position);
                CartDb cartDb = new CartDb(StoreDetails.this);
                cartDb.deleteItem(currItem.get(position).getItemId());
                v.addToCart.setVisibility(View.VISIBLE);
                v.removeFromCart.setVisibility(View.GONE);
            }

            @Override
            public void ItemOnClick(ItemListAdapter.MyViewHolder v, int position) {
                Intent intent = new Intent(StoreDetails.this,ItemsDetails.class);
                intent.putExtra("itemId",currItem.get(position).getItemId());
                intent.putExtra("itemCategory",currItem.get(position).getItemCategory());
                intent.putExtra("storeId",storeId);
                startActivity(intent);
            }
        });


        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

}