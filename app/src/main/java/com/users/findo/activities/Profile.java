package com.users.findo.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.users.findo.R;
import com.users.findo.dataClasses.User;
import com.users.findo.databinding.ActivityProfileBinding;
import com.users.findo.viewModels.UserViewModel;

public class Profile extends AppCompatActivity {

    User user;
    ActivityProfileBinding binding;
    private UserViewModel userViewModel;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getLiveUserData().observe(this, user -> {
            this.user = user;
            binding.name.setText(user.getName());
            binding.email.setText(user.getEmail());
        });
        mAuth = FirebaseAuth.getInstance();

        binding.logout.setOnClickListener(v -> {

        });
        binding.contactUs.setOnClickListener(v -> {
            startActivity(new Intent(this, ContactUs.class));
        });

    }
}