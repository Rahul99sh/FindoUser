package com.users.findo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.users.findo.R;
import com.users.findo.adapters.StoreListRvAdapter;
import com.users.findo.dataClasses.Store;
import com.users.findo.viewModels.StoreViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        String filter = getIntent().getStringExtra("filter");
        recyclerView = findViewById(R.id.AllStores_rv);
        searchView =findViewById(R.id.AllStores_searchView);
        storeViewModel = new ViewModelProvider(this).get(StoreViewModel.class);
        storeViewModel.getLiveStoreData().observe(this,stores -> {
            this.storeList = stores;
            if(filter == null || filter.isEmpty()){
                adapter = new StoreListRvAdapter(this, stores, (v, position) -> {
                    Intent i = new Intent(AllStores.this, StoreDetails.class);
                    i.putExtra("store",stores.get(position));
                    startActivity(i);
                });
            }else{
                List<Store> filteredSores = new ArrayList<>();
                for (Store s :
                        stores) {
                    if(filter.equals("Grocery") && (s.getCategory().equals(filter) || s.getCategory().equals("General Store"))){
                        filteredSores.add(s);
                    }else if(s.getCategory().equals(filter)) filteredSores.add(s);
                }
                adapter = new StoreListRvAdapter(this, filteredSores, (v, position) -> {
                    Intent i = new Intent(AllStores.this, StoreDetails.class);
                    i.putExtra("store",stores.get(position));
                    startActivity(i);
                });
            }

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