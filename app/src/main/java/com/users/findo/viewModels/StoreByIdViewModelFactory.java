package com.users.findo.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.users.findo.dataClasses.Store;

public class StoreByIdViewModelFactory implements ViewModelProvider.Factory {
    private Store store;

    public StoreByIdViewModelFactory(Store store) {
        this.store = store;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StoreByIdViewModel.class)) {
            return (T) new StoreByIdViewModel(store);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
