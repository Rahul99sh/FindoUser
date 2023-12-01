package com.users.findo.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.users.findo.dataClasses.Item;
import com.users.findo.dataClasses.Store;
import com.users.findo.repos.StoreByIdRepo;

import java.util.List;

public class StoreByIdViewModel extends ViewModel {
    MutableLiveData<List<Item>> storeItemsMutableLiveData;
    StoreByIdRepo storeRepository;
    public StoreByIdViewModel(Store s) {
        storeRepository = new StoreByIdRepo(s);
        storeItemsMutableLiveData=  storeRepository.getStoreItemsMutableLiveData();
    }

    public MutableLiveData<List<Item>> getLiveStoreItemsData() {
        return storeItemsMutableLiveData;
    }
}