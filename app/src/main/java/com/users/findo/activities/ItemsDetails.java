package com.users.findo.activities;


import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.users.findo.R;
import com.users.findo.dataClasses.CartDatabase;
import com.users.findo.databaseClass.CartDb;
import com.users.findo.databaseClass.FavDb;

import java.util.Objects;

public class ItemsDetails extends AppCompatActivity {

    String itemName;
    String itemUrl;
    String itemCategory1;
    String itemTag,itemDesc,price;
    FirebaseFirestore rootRef;

    TextView itemNameTextView,itemStore,pricetext;
    ImageView itemImageView , backImage ,cartImage, share, fav;
    ConstraintLayout constraintLayout;
    FrameLayout frameLayout;
    LinearLayout review;
    TextView addToCart;
    public CartDatabase item;
    TextView remove,desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_details);

        rootRef = FirebaseFirestore.getInstance();

        itemNameTextView = findViewById(R.id.itemNameTxt);
        addToCart = findViewById(R.id.addToCartF);
        remove = findViewById(R.id.removeFromCartF);
        itemImageView = findViewById(R.id.itemImage);
        pricetext = findViewById(R.id.price);
        backImage=findViewById(R.id.backImage);
        share=findViewById(R.id.shareItem);
        fav=findViewById(R.id.favItem);
        review = findViewById(R.id.review);
        constraintLayout = findViewById(R.id.ItemDisc);
        frameLayout = findViewById(R.id.container);
        desc = findViewById(R.id.itemDesc);
        itemStore = findViewById(R.id.itemStore);

        String storeId = getIntent().getExtras().getString("storeId");
        String itemId = getIntent().getExtras().getString("itemId");
        String itemCategory = getIntent().getExtras().getString("itemCategory");


        Dialog dialog1 = new Dialog(ItemsDetails.this);




        if(new CartDb(ItemsDetails.this).itemExist(itemId)){
            addToCart.setVisibility(View.GONE);
            remove.setVisibility(View.VISIBLE);
        }

        FavDb favDb = new FavDb(ItemsDetails.this);
        if(favDb.itemExist(itemId)){
            fav.setImageResource(R.drawable.ic_redheart);
        }else{
            fav.setImageResource(R.drawable.ic_white_heart);
        }



        rootRef.collection("Store").document(storeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task1) {

                String storeName = Objects.requireNonNull(task1.getResult().get("StoreName")).toString();
                String storeLat= Objects.requireNonNull(task1.getResult().get("StoreLat")).toString();
                String storeLong= Objects.requireNonNull(task1.getResult().get("StoreLong")).toString();
                String storeUrl = Objects.requireNonNull(task1.getResult().get("StoreUrl")).toString();
                itemStore.setText(storeName);

                rootRef.collection("Store").document(storeId).collection("Items").document(itemId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                        itemCategory1 = Objects.requireNonNull(task.getResult().get("Category")).toString();
                        itemName = Objects.requireNonNull(task.getResult().get("ItemName")).toString();
                        itemUrl = Objects.requireNonNull(task.getResult().get("ItemUrl")).toString();
                        itemTag = Objects.requireNonNull(task.getResult().get("ItemTag")).toString();
                        itemDesc = Objects.requireNonNull(task.getResult().get("ItemDescription")).toString();
                        price = Objects.requireNonNull(task.getResult().get("price")).toString();
                        desc.setText(itemDesc);
                        pricetext.setText(price);
                        itemNameTextView.setText(itemName);

                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .error(R.drawable.ic_happy);
                        Glide.with(ItemsDetails.this).load(itemUrl).apply(options).into(itemImageView);

                        item = new CartDatabase(storeId,itemId,itemName,storeName,storeUrl,itemUrl,Double.parseDouble(storeLat),Double.parseDouble(storeLong),itemCategory1,itemTag,price);
                    }
                }).addOnFailureListener(e -> {

                });
            }
        }).addOnFailureListener(e -> {

        });



        fav.setOnClickListener(view -> {
            FavDb favDb1 = new FavDb(ItemsDetails.this);
            if(favDb1.itemExist(itemId)){
                //item is already in fav list -> remove it
                favDb1.deleteItem(itemId);
                fav.setImageResource(R.drawable.ic_white_heart);
                //notify data set changed
            }else{
                //add into favDb
                favDb1.insertOneItem(item);
                fav.setImageResource(R.drawable.ic_redheart);
            }
        });



        addToCart.setOnClickListener(view ->{
            CartDb cartDb = new CartDb(ItemsDetails.this);
            cartDb.insertOneItem(item);
            addToCart.setVisibility(View.GONE);
            remove.setVisibility(View.VISIBLE);
        });

        remove.setOnClickListener(v -> {
            CartDb cartDb = new CartDb(ItemsDetails.this);
            cartDb.deleteItem(itemId);
            addToCart.setVisibility(View.VISIBLE);
            remove.setVisibility(View.GONE);
        });

        backImage.setOnClickListener(view -> {
            if(constraintLayout.getVisibility()==View.VISIBLE){
                finish();
            }else{
                constraintLayout.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
            }
        });

        cartImage = findViewById(R.id.cartBtn);



    }
    public void loadFrag(Fragment fragment){
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container , fragment );
        ft.commit();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(constraintLayout.getVisibility()==View.VISIBLE){
            finish();
        }else{
            constraintLayout.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        }
    }


}