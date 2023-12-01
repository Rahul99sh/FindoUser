package com.users.findo.repos;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.users.findo.dataClasses.Item;
import com.users.findo.dataClasses.Store;

import java.util.ArrayList;
import java.util.List;

public class StoreByIdRepo {
    FirebaseFirestore db;
    MutableLiveData<List<Item>> storeItemsMutableLiveData;
    List<Item> allItems = new ArrayList<>();
    public StoreByIdRepo(Store s){
        this.storeItemsMutableLiveData = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        getItems(s);
    }

    public void getItems(Store s){
        db.collection("Store").document(s.getStoreId()).collection("Items").addSnapshotListener((value, error) -> {
            if (error != null) {
                // Handle error
                return;
            }
            if (value != null) {
                List<Item> itemList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        Item item = doc.toObject(Item.class);
                        item.setItemId(doc.getId());
                        item.setStoreID(s.getStoreId());
                        item.setStoreUrl(s.getStoreUrl());
                        item.setStoreName(s.getStoreName());
                        item.setStoreLat(s.getStoreLat());
                        item.setStoreLong(s.getStoreLong());
                        itemList.add(item);
                    }
                }
                allItems.addAll(itemList);
                storeItemsMutableLiveData.postValue(itemList);
            }
        });
    }

    public MutableLiveData<List<Item>> getStoreItemsMutableLiveData() {
        return storeItemsMutableLiveData;
    }

}
