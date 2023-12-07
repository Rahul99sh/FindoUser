package com.users.findo.repos;

import static com.users.findo.fragments.DashboardFragment.city;
import static com.users.findo.fragments.DashboardFragment.state;
import static com.users.findo.fragments.DashboardFragment.userLat;
import static com.users.findo.fragments.DashboardFragment.userLong;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.users.findo.dataClasses.Item;
import com.users.findo.dataClasses.Store;

import java.util.ArrayList;
import java.util.List;

public class StoreRepo {
    FirebaseFirestore db;
    List<String> allStoresId;
    List<Store> allStores;
    MutableLiveData<List<Store>> storeMutableLiveData;
    MutableLiveData<List<Item>> storeItemsMutableLiveData;
    int count = 0,count1 =0;
    List<Item> allItems = new ArrayList<>();

    public StoreRepo() {
        this.storeMutableLiveData = new MutableLiveData<>();
        this.storeItemsMutableLiveData = new MutableLiveData<>();
        allStoresId = new ArrayList<>();
        allStores = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<List<Store>> getStoreMutableLiveData() {
        listenToStoreListUpdates(state, city);
        return storeMutableLiveData;
    }

    private void listenToStoreListUpdates(String state, String city) {
        db.collection("StoreList").document(state).addSnapshotListener((value, error) -> {
            if (error != null) {
                // Handle error
                return;
            }
            if (value != null) {
                allStoresId = (List<String>) value.get(city);
                if (allStoresId != null && !allStoresId.isEmpty()) {
                    listenToStoresUpdates();
                }
            }
        });
    }

    private void listenToStoresUpdates() {
        allStores.clear();
        allItems.clear();
        Location userLoc = new Location("");
        userLoc.setLatitude(userLat);
        userLoc.setLongitude(userLong);
        for (String id : allStoresId) {
            db.collection("Store").document(id).addSnapshotListener((value, error) -> {
                if (error != null) {
                    // Handle error
                    return;
                }
                if (value != null) {
                    Store s = value.toObject(Store.class);
                    if (s != null && s.isVerified()) {
                        Location storeLoc = new Location("");
                        storeLoc.setLatitude(s.getStoreLat());
                        storeLoc.setLongitude(s.getStoreLong());
                        float dist = userLoc.distanceTo(storeLoc) / 1000.0f;
                        s.setDist(dist);
                        s.setStoreId(id);
                        allStores.add(s);
                        getStoreItems(id,s);
                    }
                }
                count++;

                if (count == allStoresId.size()) {
                    storeMutableLiveData.postValue(allStores);
                }
            });
        }
    }

    private void getStoreItems(String storeId, Store s) {
        db.collection("Store").document(storeId).collection("Items").addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null) {
                List<Item> itemList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        Item item = doc.toObject(Item.class);
                        item.setItemId(doc.getId());
                        item.setStoreID(storeId);
                        item.setStoreUrl(s.getStoreUrl());
                        item.setStoreName(s.getStoreName());
                        item.setStoreLat(s.getStoreLat());
                        item.setStoreLong(s.getStoreLong());
                        itemList.add(item);
                    }
                }
                allItems.addAll(itemList);
                count1++;
//                if(count1 == allStoresId.size()){
                    storeItemsMutableLiveData.postValue(allItems);
//                }
            }

        });
    }

    public MutableLiveData<List<Item>> getStoreItemsMutableLiveData() {
        return storeItemsMutableLiveData;
    }
}
