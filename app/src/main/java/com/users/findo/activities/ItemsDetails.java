package com.users.findo.activities;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
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
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.users.findo.R;
import com.users.findo.dataClasses.CartDatabase;
import com.users.findo.dataClasses.Item;
import com.users.findo.databaseClass.CartDb;
import com.users.findo.databaseClass.FavDb;

import java.util.Objects;

public class ItemsDetails extends AppCompatActivity {

    FirebaseFirestore rootRef;

    TextView itemNameTextView,itemStore,pricetext;
    ImageView itemImageView , backImage ,cartImage, share, fav;
    ConstraintLayout constraintLayout;
    LinearLayout review;
    TextView addToCart;
    TextView remove,desc;
    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_details);
        item = getIntent().getParcelableExtra("item");
        rootRef = FirebaseFirestore.getInstance();
        MaterialButton vr = findViewById(R.id.virtual);
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
        desc = findViewById(R.id.itemDesc);
        itemStore = findViewById(R.id.itemStore);

        if(item.isArEnabled()){
            vr.setVisibility(View.VISIBLE);
        }else{
            vr.setVisibility(View.GONE);
        }

        vr.setOnClickListener(v -> {
            Intent sceneViewerIntent = new Intent(Intent.ACTION_VIEW);
            Uri.Builder intentUri = Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon();
            intentUri.appendQueryParameter("file", item.getArModelLink())
                    .appendQueryParameter("mode", "ar_only")
                    .appendQueryParameter("resizable", "false")
                    .appendQueryParameter("title", item.getItemName() + " - ₹" + item.getPrice());
            sceneViewerIntent.setData(intentUri.build());
            sceneViewerIntent.setPackage("com.google.ar.core");
            startActivity(sceneViewerIntent);

        });

        if(new CartDb(ItemsDetails.this).itemExist(item.getItemId())){
            addToCart.setVisibility(View.GONE);
            remove.setVisibility(View.VISIBLE);
        }

        FavDb favDb = new FavDb(ItemsDetails.this);
        if(favDb.itemExist(item.getItemId())){
            fav.setImageResource(R.drawable.ic_redheart);
        }else{
            fav.setImageResource(R.drawable.ic_white_heart);
        }
        itemStore.setText(item.getStoreName());


        desc.setText(item.getItemDescription());
        pricetext.setText(item.getPrice());
        itemNameTextView.setText(item.getItemName());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_happy);
        Glide.with(ItemsDetails.this).load(item.getItemUrl()).apply(options).into(itemImageView);



        fav.setOnClickListener(view -> {
            FavDb favDb1 = new FavDb(ItemsDetails.this);
            if(favDb1.itemExist(item.getItemId())){
                //item is already in fav list -> remove it
                favDb1.deleteItem(item.getItemId());
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
            cartDb.deleteItem(item.getItemId());
            addToCart.setVisibility(View.VISIBLE);
            remove.setVisibility(View.GONE);
        });

        backImage.setOnClickListener(view -> {
            finish();
        });

        cartImage = findViewById(R.id.cartBtn);



    }


}