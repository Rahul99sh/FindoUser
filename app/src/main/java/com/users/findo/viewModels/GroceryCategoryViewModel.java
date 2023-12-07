package com.users.findo.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.users.findo.dataClasses.Category;
import com.users.findo.repos.GroceryCategoryRepo;

import java.util.List;


public class GroceryCategoryViewModel extends ViewModel {
    private final LiveData<List<Category>> categoryLiveData;

    public GroceryCategoryViewModel() {
        GroceryCategoryRepo categoryRepo = new GroceryCategoryRepo();
        categoryLiveData = categoryRepo.getCategoryMutableLiveData();
    }

    public LiveData<List<Category>> getCategoryLiveData() {
        return categoryLiveData;
    }
}
