package com.users.findo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.users.findo.activities.Maintainance;
import com.users.findo.activities.Profile;
import com.users.findo.databinding.ActivityMainBinding;
import com.users.findo.fragments.CartFragment;
import com.users.findo.fragments.DashboardFragment;
import com.users.findo.fragments.FavoritesFragment;
import com.users.findo.viewModels.MaintainanceViewModel;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    public static ChipNavigationBar chipNavigationBar;
    public DrawerLayout drawerLayout;
    MaintainanceViewModel viewModel1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        binding.profile.setOnClickListener(v -> {
            startActivity(new Intent(this, Profile.class));
        });
        binding.menu.setOnClickListener(v -> {
            if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(Gravity.LEFT); //CLOSE Nav Drawer!
            }else{
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        initialiseProperties();
        viewModel1 = new ViewModelProvider(this).get(MaintainanceViewModel.class);
        viewModel1.loadData();
        viewModel1.getData().observe(this, data -> {
            if(data.isMaintainance_user()){
                Intent i =  new Intent(this, Maintainance.class);
                startActivity(i);
                finish();
            }else{
                change_Activity(new DashboardFragment());
                chipNavigationBar.setItemSelected(R.id.home,true);
            }
        });



        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if(itemId == R.id.home1) {
                // For R.id.home1
                // change_Activity(new DashboardFragment(MainActivity.this));
                // chipNavigationBar.setItemSelected(R.id.home,true);
            } else if(itemId == R.id.settings1) {
                // For R.id.settings1
                Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
            } else if(itemId == R.id.explore1) {
                // For R.id.explore1
                // Intent i = new Intent(MainActivity.this, ShopOwnerWelcome.class);
                // startActivity(i);
            } else if(itemId == R.id.contactus) {
                // For R.id.contactus
                // Intent i1 = new Intent(MainActivity.this, Contact.class);
                // startActivity(i1);
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });


        chipNavigationBar.setOnItemSelectedListener(i -> {
            if (i == R.id.home) {
                change_Activity(new DashboardFragment());
            } else if (i == R.id.favorites) {
                change_Activity(new FavoritesFragment());
            } else if (i == R.id.settings) {
                change_Activity(new CartFragment());
            }

        });




    }



    private void initialiseProperties() {
        chipNavigationBar = findViewById(R.id.bottom_nav_bar);
    }

    //swap fragment
    private void change_Activity(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }
}