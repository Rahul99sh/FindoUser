package com.users.findo.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.users.findo.dataClasses.Store;
import com.users.findo.R;
import com.users.findo.adapters.StoreListRvAdapter;
import com.users.findo.viewModels.StoreViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AllStores extends AppCompatActivity {
    RecyclerView recyclerView;
    SearchView searchView;
    private List<Store> storeList = new ArrayList<>();

    StoreListRvAdapter adapter;
    StoreViewModel storeViewModel;
    ArrayList<Store> filteredStoreList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stores);
        recyclerView = findViewById(R.id.AllStores_rv);
        searchView =findViewById(R.id.AllStores_searchView);
        storeViewModel = new ViewModelProvider(this).get(StoreViewModel.class);
        storeViewModel.getLiveStoreData().observe(this,stores -> {
            this.storeList = stores;
            adapter = new StoreListRvAdapter(this, stores, (v, position) -> {
                Intent i = new Intent(AllStores.this, StoreDetails.class);
                i.putExtra("store",stores.get(position));
                startActivity(i);
            });
            recyclerView.setAdapter(adapter);
        });





        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<Store> filteredItemList = new ArrayList<>();
                filteredItemList.addAll(filterStore(query));
                adapter.setFilteredItemList(filteredItemList);
                adapter.notifyDataSetChanged();
                if(filteredItemList.isEmpty()){
                    // hide this Rv
                    recyclerView.setVisibility(View.GONE);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Store> filteredItemList = new ArrayList<>();
                filteredItemList.addAll(filterStore(newText));
                adapter.setFilteredItemList(filteredItemList);
                adapter.notifyDataSetChanged();
                if(filteredItemList.isEmpty()){
                    // hide this Rv
                    recyclerView.setVisibility(View.GONE);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

    }

    private ArrayList<Store> filterStore(String q){

        List<Store> items = storeList;

        filteredStoreList.clear();
        for(Store item : items){
            if(item.getStoreName().toLowerCase(Locale.ROOT).contains(q.toLowerCase(Locale.ROOT))){
                filteredStoreList.add(item);
            }
        }
        //update filtered list

        return filteredStoreList;

    }




}