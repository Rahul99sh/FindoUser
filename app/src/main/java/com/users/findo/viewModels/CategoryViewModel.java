package com.users.findo.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.users.findo.dataClasses.Category;
import com.users.findo.repos.CategoryRepo;

import java.util.List;

public class CategoryViewModel extends ViewModel {
    private CategoryRepo categoryRepo;
    private LiveData<List<Category>> categoryLiveData;

    public CategoryViewModel() {
        categoryRepo = new CategoryRepo();
        categoryLiveData = categoryRepo.getCategoryMutableLiveData();
    }

    public LiveData<List<Category>> getCategoryLiveData() {
        return categoryLiveData;
    }
}
