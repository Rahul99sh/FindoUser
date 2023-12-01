package com.users.findo.repos;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.users.findo.dataClasses.Promotion;

import java.util.ArrayList;
import java.util.List;

public class PromotionRepo {
    FirebaseFirestore db;
    MutableLiveData<List<Promotion>> promotionMutableList;
    List<PromotionRepo> allPromo;
    public PromotionRepo(){
        this.promotionMutableList = new MutableLiveData<>();
        allPromo = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<List<Promotion>> getPromotionMutableLiveData(){
        db.collection("Promotion").addSnapshotListener((value, error) -> {
            if (error != null) {
                // Handle error
                return;
            }
            if (value != null) {
                List<Promotion> promotions = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        Promotion promotion = doc.toObject(Promotion.class);
                        promotions.add(promotion);
                    }
                }
                promotionMutableList.postValue(promotions);
            }
        });
        return promotionMutableList;
    }
}
