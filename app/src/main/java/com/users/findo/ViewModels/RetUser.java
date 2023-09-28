package com.users.findo.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.users.findo.dataClasses.User;

import java.util.Objects;

public class RetUser extends ViewModel {
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final DocumentReference userRef;
    private ListenerRegistration guestListenerRegistration;

    public RetUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRef = db.collection("Users").document(currentUserId);
    }

    public void fetchUser(){
        guestListenerRegistration = userRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                // Handle error
                return;
            }
            if (value != null && value.exists()) {
                User user = value.toObject(User.class);
//                if (user != null) {
//                    user.setId(value.getId());
//                }
                userLiveData.postValue(user);
            } else {
                userLiveData.postValue(null);
            }
        });
    }
    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }
    private void stopListeningData(){
        if (guestListenerRegistration != null) {
            guestListenerRegistration.remove();
        }
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        stopListeningData();
    }
}
