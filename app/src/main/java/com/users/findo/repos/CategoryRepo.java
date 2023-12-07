package com.users.findo.repos;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.users.findo.dataClasses.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepo {
    FirebaseFirestore db;
    List<Category> categoryList;
    MutableLiveData<List<Category>> categoryMutableLiveData;

    public CategoryRepo(){
        db = FirebaseFirestore.getInstance();
        categoryMutableLiveData = new MutableLiveData<>();
        categoryList = new ArrayList<>();
        getCategoriesListUpdates();
    }

    private void getCategoriesListUpdates() {
        db.collection("Categories").addSnapshotListener((value, error) -> {
            if (error != null) {
                // Handle error
                return;
            }
            if (value != null) {
                categoryList.clear();
                for (QueryDocumentSnapshot doc :
                        value) {
                    Category c = doc.toObject(Category.class);

                    categoryList.add(c);
                }
                categoryMutableLiveData.postValue(categoryList);
            }
        });
    }
    public MutableLiveData<List<Category>> getCategoryMutableLiveData() {
        return categoryMutableLiveData;
    }
}
