package com.users.findo.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.users.findo.R;
import com.users.findo.databinding.ActivityOnboardingBinding;

public class Onboarding extends AppCompatActivity {

    ActivityOnboardingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding);
        binding.login.setOnClickListener(view -> {
            Intent i = new Intent(Onboarding.this, Login.class);
            startActivity(i);
            finish();
        });
        binding.register.setOnClickListener(view -> {
            Intent i = new Intent(Onboarding.this, Registration.class);
            startActivity(i);
            finish();
        });
    }
}