package com.users.findo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import com.users.findo.dataClasses.User;
import com.users.findo.viewModels.RetUser;
import com.users.findo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    RetUser userViewModel;
    User user;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        userViewModel = new ViewModelProvider(this).get(RetUser.class);

        userViewModel.fetchUser();
        userViewModel.getUserLiveData().observe(this, user1 -> {
            this.user = user1;
            binding.setUser(user);
        });

        binding.sampleButton.setOnClickListener(v->{

        });
    }
}