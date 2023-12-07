package com.users.findo.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.users.findo.R;
import com.users.findo.dataClasses.User;
import com.users.findo.databinding.ActivityProfileBinding;
import com.users.findo.security.Crypto;
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
        mAuth = FirebaseAuth.getInstance();
        Crypto crypto = new Crypto();
        userViewModel.getLiveUserData().observe(this, user -> {
            this.user = user;
            binding.name.setText(crypto.decrypt(user.getName()));
            binding.email.setText(crypto.decrypt(user.getEmail()));
            binding.referral.setText(crypto.decrypt(user.getReferralCode()));
            Glide.with(this).load(crypto.decrypt(user.getImage())).into(binding.circleImageView);
        });


        binding.logout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent i = new Intent(this, Onboarding.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("EXIT", true);
            startActivity(i);
            finish();
        });
        binding.contactUs.setOnClickListener(v -> {
            startActivity(new Intent(this, ContactUs.class));
        });

    }
}