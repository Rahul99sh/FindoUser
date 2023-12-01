package com.users.findo.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.users.findo.dataClasses.Store;
import com.users.findo.dataClasses.Item;
import com.users.findo.repos.StoreRepo;

import java.util.List;

public class StoreViewModel extends ViewModel {
    MutableLiveData<List<Store>> storeMutableLiveData;
    MutableLiveData<List<Item>> storeItemsMutableLiveData;
    FirebaseFirestore mFirestore;
    StoreRepo storeRepository;

    public StoreViewModel() {
        storeRepository = new StoreRepo();
        storeMutableLiveData=  storeRepository.getStoreMutableLiveData();
        storeItemsMutableLiveData=  storeRepository.getStoreItemsMutableLiveData();
        mFirestore = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<List<Store>> getLiveStoreData() {
        return storeMutableLiveData;
    }
    public MutableLiveData<List<Item>> getLiveStoreItemsData() {
        return storeItemsMutableLiveData;
    }
}