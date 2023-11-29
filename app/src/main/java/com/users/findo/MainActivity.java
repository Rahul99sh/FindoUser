package com.users.findo;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.users.findo.dataClasses.User;
import com.users.findo.databinding.ActivityMainBinding;
import com.users.findo.fragments.CartFragment;
import com.users.findo.fragments.CategoriesFragment;
import com.users.findo.fragments.DashboardFragment;
import com.users.findo.fragments.FavoritesFragment;
import com.users.findo.viewModels.RetUser;

public class MainActivity extends AppCompatActivity {
    RetUser userViewModel;
    User user;
    ActivityMainBinding binding;
    public static ChipNavigationBar chipNavigationBar;
    public DrawerLayout drawerLayout;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        userViewModel = new ViewModelProvider(this).get(RetUser.class);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        ImageView menu = findViewById(R.id.menu);



        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(Gravity.LEFT); //CLOSE Nav Drawer!
                }else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        initialiseProperties();




        change_Activity(new DashboardFragment());
        chipNavigationBar.setItemSelected(R.id.home,true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
        });


        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                if (i == R.id.home) {
                    change_Activity(new DashboardFragment());
                } else if (i == R.id.activity) {
                    change_Activity(new CategoriesFragment());
                } else if (i == R.id.favorites) {
                    change_Activity(new FavoritesFragment());
                } else if (i == R.id.settings) {
                    change_Activity(new CartFragment());
                }

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