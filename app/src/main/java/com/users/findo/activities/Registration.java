package com.users.findo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.users.findo.MainActivity;
import com.users.findo.R;
import com.users.findo.bottomSheets.LoadingSheet;
import com.users.findo.databinding.ActivityRegistrationBinding;

import java.util.HashMap;
import java.util.Objects;

public class Registration extends AppCompatActivity {

    ActivityRegistrationBinding binding;
    FirebaseAuth mAuth;
    LoadingSheet loadingSheet;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingSheet = new LoadingSheet("Creating Account...");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        binding.register.setOnClickListener(view -> {
            createUser();
        });
        binding.login.setOnClickListener(view -> {
            Intent i = new Intent(Registration.this, Login.class);
            startActivity(i);
            finish();
        });
    }
    private void createUser() {
        // Code for creating a user id for registration
        String email = Objects.requireNonNull(binding.email.getText()).toString();
        String password = Objects.requireNonNull(binding.password.getText()).toString();
        String cpassword = Objects.requireNonNull(binding.cPassword.getText()).toString();

        if(TextUtils.isEmpty(email)){
            binding.emailBox.setError("Email cannot be empty");
            binding.email.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            binding.passwordBox.setError("Password cannot be empty");
            binding.password.requestFocus();
        }else if(!password.equals(cpassword)){
            binding.cPasswordBox.setError("Password does not match");
            binding.cPassword.requestFocus();
        }
        else{
            loadingSheet.show(getSupportFragmentManager(), loadingSheet.getTag());
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    HashMap<String,String> mp = new HashMap<>();
                    mp.put("OwnerId", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                    mp.put("email", email);
                    db.collection("ShopOwners").document(mAuth.getCurrentUser().getUid()).set(mp).addOnCompleteListener(task1 -> {
                        loadingSheet.dismiss();
                        Toast.makeText(Registration.this , "Registered successfully" , Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Registration.this, MainActivity.class)); // redirecting to login page
                        finish();
                    });
                }else{
                    loadingSheet.dismiss();
                    Toast.makeText(Registration.this , "Registration Error" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}