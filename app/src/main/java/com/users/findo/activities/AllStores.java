package com.users.findo.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.users.findo.dataClasses.Store;
import com.users.findo.R;
import com.users.findo.adapters.StoreListRvAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class AllStores extends AppCompatActivity {
    RecyclerView recyclerView;
    SearchView searchView;
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private  ArrayList<Store> storeList = new ArrayList<>();
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    StoreListRvAdapter adapter;

    ArrayList<Store> filteredStoreList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stores);
        recyclerView = findViewById(R.id.AllStores_rv);
        searchView =findViewById(R.id.AllStores_searchView);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    rootRef.collection("Store")
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    //find curr loc and calc dist b/w you and store and sort list
                                    Location location1 = new Location("");
                                    location1.setLatitude(latitude);
                                    location1.setLongitude(longitude);
                                    storeList.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Location location2 = new Location("");
                                        double lat = Objects.requireNonNull(document.getDouble("StoreLat"));
                                        double long1 = Objects.requireNonNull(document.getDouble("StoreLong"));
                                        location2.setLatitude(lat);
                                        location2.setLongitude(long1);
                                        float dist = location1.distanceTo(location2) / 1000.0f;
                                        String distance = String.format("%.2f", dist) + " km";
                                        Store store = new Store(document.getString("StoreName"),
                                                document.getDouble("StoreLat"),
                                                document.getDouble("StoreLong"),
                                                document.getString("StoreUrl"),
                                                distance, dist, document.getString("StoreId"));
                                        storeList.add(store);
                                    }
                                    // Define a custom Comparator to sort the MyObject instances by ID
                                    Comparator<Store> comparator = new Comparator<Store>() {
                                        @Override
                                        public int compare(Store o1, Store o2) {
                                            return Integer.compare(Float.valueOf(o1.getDist() * 100).intValue(), Float.valueOf(o2.getDist() * 100).intValue());
                                        }
                                    };

                                    // Sort the ArrayList using the custom Comparator
                                    storeList.sort(comparator);

                                    adapter = new StoreListRvAdapter(AllStores.this, storeList, (v, position) -> {
                                        Intent intent = new Intent(AllStores.this, StoreDetails.class);
                                        intent.putExtra("storeId", storeList.get(position).getStoreId());
                                        startActivity(intent);
                                    });
                                    recyclerView.setAdapter(adapter);
                                }
                            }).addOnFailureListener(e -> {
                            });
                }
            }
        };


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<Store> filteredItemList;
                filteredItemList = filterStore(query);
                adapter.setFilteredItemList(filteredItemList);
                adapter.notifyDataSetChanged();
                if(filteredItemList.isEmpty()){
                    // hide this Rv
                    recyclerView.setVisibility(View.GONE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Store> filteredItemList;
                filteredItemList = filterStore(newText);
                adapter.setFilteredItemList(filteredItemList);
                adapter.notifyDataSetChanged();
                if(filteredItemList.isEmpty()){
                    // hide this Rv
                    recyclerView.setVisibility(View.GONE);
                }
                return true;
            }
        });

    }

    private ArrayList<Store> filterStore(String q){

        ArrayList<Store> items = storeList;

        filteredStoreList.clear();
        for(Store item : items){
            if(item.getStoreName().toLowerCase(Locale.ROOT).contains(q.toLowerCase(Locale.ROOT))){
                filteredStoreList.add(item);
            }
        }
        //update filtered list

        return filteredStoreList;

    }

    @Override
    public void onStart() {
        super.onStart();
        requestLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        removeLocationUpdates();
    }


    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request the user to grant location permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // Create a LocationRequest object
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            // Request location updates
            mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.getMainLooper());
        }
    }

    private void removeLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                Toast.makeText(AllStores.this, "Location permission is required.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}