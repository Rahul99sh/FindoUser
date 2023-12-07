package com.users.findo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.users.findo.MainActivity;
import com.users.findo.R;
import com.users.findo.security.Crypto;

public class FirstScreen extends AppCompatActivity {
    ImageView logo;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        logo = findViewById(R.id.logo);

        new Handler().postDelayed(() -> {
            Intent i;
            if(mUser != null) {
                i = new Intent(FirstScreen.this, MainActivity.class);
            }else{
                i = new Intent(FirstScreen.this, Onboarding.class);
            }
            startActivity(i);
            overridePendingTransition(R.anim.no_animation, R.anim.fade_out);
            finish();
        }, 3000);

    }

}