package com.users.findo.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.users.findo.R;
import com.users.findo.databinding.ActivityContactUsBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ContactUs extends AppCompatActivity {

    ActivityContactUsBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseFirestore db;
    Uri selectedImageUri;
    FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_contact_us);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("Query");
        binding.email.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
        binding.sendQuery.setOnClickListener(v -> {
            String name = Objects.requireNonNull(binding.name.getText()).toString();
            String sub = Objects.requireNonNull(binding.subject.getText()).toString();
            String feedback = Objects.requireNonNull(binding.query.getText()).toString();
            if(name.equals("") || sub.equals("") || feedback.equals("")){
                Toast.makeText(this, "All fields are Required!", Toast.LENGTH_SHORT).show();
            }else if(selectedImageUri != null){
                uploadImage(name,sub,feedback);
            }else{
                writeTODb(name,null,sub,feedback);
            }
        });
        binding.addImg.setOnClickListener(v -> {
            pickImageFromGallery();
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
            binding.selImg.setVisibility(View.VISIBLE);
            binding.selImg.setImageURI(selectedImageUri);
        }
    }
    public void uploadImage(String s, String sub, String feeedback) {
        StorageReference imageRef = storageReference.child(Objects.requireNonNull(selectedImageUri.getLastPathSegment()));
        imageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    String imageUrl = downloadUri.toString();
                    writeTODb(s,imageUrl,sub,feeedback);
                }));
    }

    private void writeTODb(String s, String imageUrl, String sub, String feeedback) {
        CollectionReference collectionReference = db.collection("Query");
        DocumentReference documentReference = collectionReference.document();
        Timestamp currentTimestamp = Timestamp.now();

        String docId = documentReference.getId();
        Map<String, Object> data = new HashMap<>();
        data.put("ticketId", docId);
        if(imageUrl != null)
            data.put("imageUrl", imageUrl);
        data.put("name", s);
        data.put("userId", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        data.put("email", Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
        data.put("feedback", feeedback);
        data.put("subject", sub);
        data.put("creationTime", currentTimestamp);
        documentReference.set(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Ticket Created", Toast.LENGTH_SHORT).show();
//                        db.collection("Store")
//                                .document(store.getStoreId())
//                                .update("promoIds", FieldValue.arrayUnion(docId));
                    } else {
                        Log.e("FirestoreExample", "Error adding document", task.getException());
                    }
                });
    }
}