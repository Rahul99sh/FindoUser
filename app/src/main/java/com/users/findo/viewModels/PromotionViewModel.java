package com.users.findo.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.users.findo.dataClasses.Promotion;
import com.users.findo.repos.PromotionRepo;

import java.util.List;

public class PromotionViewModel extends ViewModel {
    private PromotionRepo promotionRepo;
    private LiveData<List<Promotion>> promotionLiveData;

    public PromotionViewModel() {
        promotionRepo = new PromotionRepo();
        promotionLiveData = promotionRepo.getPromotionMutableLiveData();
    }

    public LiveData<List<Promotion>> getPromotions() {
        return promotionLiveData;
    }
}
