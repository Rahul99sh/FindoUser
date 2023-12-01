package com.users.findo.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.users.findo.R;
import com.users.findo.adapters.ItemListAdapter;
import com.users.findo.dataClasses.CartDatabase;
import com.users.findo.dataClasses.Item;
import com.users.findo.dataClasses.Store;
import com.users.findo.databaseClass.CartDb;
import com.users.findo.databaseClass.FavDb;
import com.users.findo.repos.StoreByIdRepo;
import com.users.findo.viewModels.StoreByIdViewModel;
import com.users.findo.viewModels.StoreByIdViewModelFactory;
import com.users.findo.viewModels.StoreViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StoreDetails extends AppCompatActivity{

    TextView storename;
    StoreByIdViewModel storeByIdViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_details);
        storename = findViewById(R.id.storeName);
        RecyclerView recyclerView = findViewById(R.id.itemsRV);
        Store store = Objects.requireNonNull(getIntent().getParcelableExtra("store"));
        StoreByIdViewModelFactory factory = new StoreByIdViewModelFactory(store);
        storeByIdViewModel = new ViewModelProvider(this, factory).get(StoreByIdViewModel.class);

        storeByIdViewModel.getLiveStoreItemsData().observe(this, currItem -> {
            storename.setText(store.getStoreName());

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
                        favDb.insertOneItem(currItem.get(position));
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
                    intent.putExtra("item",currItem.get(position));
                    startActivity(intent);
                }
            });


            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        });





    }

}