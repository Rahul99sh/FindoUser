package com.users.findo.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.users.findo.MainActivity;
import com.users.findo.R;
import com.users.findo.bottomSheets.LoadingSheet;
import com.users.findo.databinding.ActivityRegistrationBinding;
import com.users.findo.security.Crypto;

import java.util.HashMap;
import java.util.Objects;

public class Registration extends AppCompatActivity {

    ActivityRegistrationBinding binding;
    FirebaseAuth mAuth;
    LoadingSheet loadingSheet;
    FirebaseFirestore db;
    Uri selectedImageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingSheet = new LoadingSheet("Creating Account...");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("UserProfile");
        binding.register.setOnClickListener(view -> {
            String email = Objects.requireNonNull(binding.email.getText()).toString();
            String password = Objects.requireNonNull(binding.password.getText()).toString();
            String name = Objects.requireNonNull(binding.name.getText()).toString();
            String cpassword = Objects.requireNonNull(binding.cPassword.getText()).toString();
            if(TextUtils.isEmpty(email)){
                binding.emailBox.setError("Email cannot be empty");
                binding.email.requestFocus();
            }else if (TextUtils.isEmpty(password)){
                binding.passwordBox.setError("Password cannot be empty");
                binding.password.requestFocus();
            }else if (TextUtils.isEmpty(name)){
                binding.nameBox.setError("Name cannot be empty");
                binding.name.requestFocus();
            } else if(!password.equals(cpassword)){
                binding.cPasswordBox.setError("Password does not match");
                binding.cPassword.requestFocus();
            }
            else {
                loadingSheet.show(getSupportFragmentManager(), loadingSheet.getTag());
                if (selectedImageUri != null) {
                    uploadImage();
                } else {
                    createUser("");
                }
            }

        });
        binding.login.setOnClickListener(view -> {
            Intent i = new Intent(Registration.this, Login.class);
            startActivity(i);
            finish();
        });
        binding.circleImageView.setOnClickListener(v -> {
            pickImageFromGallery();
        });
    }
    private void createUser(String url) {
        // Code for creating a user id for registration
        String email = Objects.requireNonNull(binding.email.getText()).toString();
        String password = Objects.requireNonNull(binding.password.getText()).toString();
        String name = Objects.requireNonNull(binding.name.getText()).toString();
        Crypto crypto = new Crypto();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    HashMap<String,String> mp = new HashMap<>();
                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(task1 -> {
                                if (!task1.isSuccessful()) {
                                    return;
                                }
                                // Get new FCM registration token
                                String registrationToken = task1.getResult();
                                mp.put("deviceToken", registrationToken);
                                mp.put("userId", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                                mp.put("email", crypto.encrypt(email));
                                mp.put("name", crypto.encrypt(name));
                                mp.put("image", crypto.encrypt(url));
                                mp.put("referralCode", crypto.encrypt(email));
                                db.collection("Users").document(mAuth.getCurrentUser().getUid()).set(mp).addOnCompleteListener(task2 -> {
                                    loadingSheet.dismiss();
                                    Toast.makeText(Registration.this , "Registered successfully" , Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Registration.this, MainActivity.class)); // redirecting to login page
                                    finish();
                                });
                            });
                }else{
                    loadingSheet.dismiss();
                    Toast.makeText(Registration.this , "Registration Error" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void pickImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            binding.circleImageView.setImageURI(selectedImageUri);
        }
    }
    public void uploadImage() {
        StorageReference imageRef = storageReference.child(Objects.requireNonNull(selectedImageUri.getLastPathSegment()));
        imageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    String imageUrl = downloadUri.toString();
                    // Display the download URL in a Toast
                    createUser(imageUrl);
                }));
    }
}