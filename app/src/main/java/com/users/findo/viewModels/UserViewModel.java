package com.users.findo.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.users.findo.dataClasses.User;
import com.users.findo.repos.UserRepo;

public class UserViewModel extends ViewModel {
    MutableLiveData<User> userMutableLiveData;
    FirebaseFirestore mFirestore;
    UserRepo userRepository;

    public UserViewModel() {
        userRepository = new UserRepo();
        userMutableLiveData=  userRepository.getUserMutableLiveData();
        mFirestore = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<User> getLiveUserData() {
        return userMutableLiveData;
    }
}