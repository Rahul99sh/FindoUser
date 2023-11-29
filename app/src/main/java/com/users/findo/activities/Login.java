package com.users.findo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.users.findo.MainActivity;
import com.users.findo.R;
import com.users.findo.bottomSheets.LoadingSheet;
import com.users.findo.databinding.ActivityLoginBinding;

import java.util.Objects;

public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth mAuth;
    LoadingSheet loadingSheet;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingSheet = new LoadingSheet("Verifying Details...");
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.login.setOnClickListener(view -> {
            loginUser();
        });
        binding.newAccount.setOnClickListener(view -> {
            Intent i = new Intent(Login.this, Registration.class);
            startActivity(i);
            finish();
        });
    }

    private void loginUser() {
        String email = Objects.requireNonNull(binding.email.getText()).toString();
        String password = Objects.requireNonNull(binding.password.getText()).toString();

        if (TextUtils.isEmpty(email)) {
            binding.emailBox.setError("Email cannot be empty");
            binding.email.requestFocus();
        }else if (TextUtils.isEmpty(password)) {
            binding.passwordBox.setError("Password cannot be empty");
            binding.password.requestFocus();
        }else{
            loadingSheet.show(getSupportFragmentManager(), loadingSheet.getTag());
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((OnCompleteListener<AuthResult>) task -> {
                if (task.isSuccessful()) {
                    db.collection("ShopOwners").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            loadingSheet.dismiss();
                            if(documentSnapshot.exists()){
                                Intent i = new Intent(Login.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }else{
                                Toast.makeText(Login.this, "This Account is not associated with Findo Shop", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                            }
                        }
                    }).addOnFailureListener(e -> {
                        loadingSheet.dismiss();
                        Toast.makeText(Login.this, "This Account is not associated with Findo Shop", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    });
                } else {
                    Toast.makeText(Login.this, "Log in Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}