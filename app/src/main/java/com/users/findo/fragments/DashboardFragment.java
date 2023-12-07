package com.users.findo.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.users.findo.R;
import com.users.findo.activities.AllStores;
import com.users.findo.activities.FilterItems;
import com.users.findo.activities.StoreDetails;
import com.users.findo.adapters.CategoryAdapter;
import com.users.findo.adapters.SliderAdapter;
import com.users.findo.adapters.StoreListRvAdapter;
import com.users.findo.dataClasses.Item;
import com.users.findo.dataClasses.Promotion;
import com.users.findo.dataClasses.Store;
import com.users.findo.databinding.FragmentDashboardBinding;
import com.users.findo.viewModels.CategoryViewModel;
import com.users.findo.viewModels.PromotionViewModel;
import com.users.findo.viewModels.StoreViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class DashboardFragment extends Fragment {


    private FusedLocationProviderClient mFusedLocationClient;
    public Geocoder geocoder;
    private LocationCallback mLocationCallback;
    public static String state, city;
    List<Item>featuredItems, newItems;
    public static List<Item>allItems;
    public static List<Store>allStores;
    private Handler handler;
    FragmentDashboardBinding binding;
    public static double userLat,userLong;
    PromotionViewModel promotionViewModel;
    StoreViewModel storeViewModel;
    CategoryViewModel categoryViewModel;
    CategoryAdapter categoryAdapter;
    StoreListRvAdapter storeListRvAdapter;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            binding.imageSlider.setCurrentItem(binding.imageSlider.getCurrentItem() + 1);
        }
    };
    public DashboardFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(Objects.requireNonNull(Looper.myLooper()));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        geocoder = new Geocoder(requireContext(),Locale.getDefault());
        featuredItems = new ArrayList<>();
        newItems = new ArrayList<>();
        allItems = new ArrayList<>();
        allStores = new ArrayList<>();
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getCategoryLiveData().observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter = new CategoryAdapter(requireContext(),categories);
            binding.categoryRv.setAdapter(categoryAdapter);
        });
        promotionViewModel = new ViewModelProvider(this).get(PromotionViewModel.class);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    userLat = location.getLatitude();
                    userLong = location.getLongitude();
                    try {
                        List<Address> addresses = geocoder.getFromLocation(userLat, userLong, 1);
                        if (addresses != null && addresses.size() > 0) {
                            Address address = addresses.get(0);
                            city = address.getLocality();
                            state = address.getAdminArea();
                            String area = address.getSubLocality();
                            String loc = area+", "+city;
                            binding.location.setText(loc);
                            setupViewModel();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        binding.seeALLStore.setOnClickListener(v -> {
            Intent i = new Intent(requireContext(), AllStores.class);
            startActivity(i);
        });

        binding.SearchBox.setOnClickListener(v -> {
            Intent i = new Intent(requireContext(), FilterItems.class);
            startActivity(i);
        });

        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    private void setupViewModel(){
        //get promotions
        promotionViewModel.getPromotions().observe(getViewLifecycleOwner(), this::setupSlideBar);
        storeViewModel = new ViewModelProvider(this).get(StoreViewModel.class);
        //get stores
        storeViewModel.getLiveStoreData().observe(getViewLifecycleOwner(), stores -> {
            binding.shimmerFrag.stopShimmer();
            binding.shimmerFrag.setVisibility(View.GONE);
            binding.mainView.setVisibility(View.VISIBLE);
            allStores = stores;
            stores.sort((store1, store2) -> Float.compare(store1.getDist(), store2.getDist()));
            List<Store> top5Stores = stores.subList(0, Math.min(stores.size(), 5));
            storeListRvAdapter = new StoreListRvAdapter(requireContext(), top5Stores, (v, position) -> {
                Intent i = new Intent(requireContext(), StoreDetails.class);
                i.putExtra("store",top5Stores.get(position));
                startActivity(i);
            });
            binding.storeRv.setAdapter(storeListRvAdapter);
        });
        storeViewModel.getLiveStoreItemsData().observe(getViewLifecycleOwner(), items -> {
            allItems= items;
        });
    }

    private void setupSlideBar(List<Promotion> promotions) {
        List<String> imageList = new ArrayList<>();
        for (Promotion promo : promotions) {
            if (promo.getCategory().equals("general")) {
                imageList.add(promo.getImageUrl());
            }
        }

        SliderAdapter sliderAdapter = new SliderAdapter(binding.imageSlider, requireContext(), imageList);

        binding.imageSlider.setAdapter(sliderAdapter);
        binding.imageSlider.setOffscreenPageLimit(3);
        binding.imageSlider.setClipToPadding(false);
        binding.imageSlider.setClipChildren(false);
        binding.imageSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        setSliderTransformer();
        binding.indicator.setViewPager(binding.imageSlider);
        sliderAdapter.registerAdapterDataObserver(binding.indicator.getAdapterDataObserver());
        binding.imageSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable );
                if(position == imageList.size()-1){
                    handler.postDelayed(()->{
                        binding.imageSlider.post(() -> binding.imageSlider.setCurrentItem(0));
                    },5000);
                }else{
                    handler.postDelayed(runnable, 5000);
                }

            }
        });
    }
    private void setSliderTransformer() {
        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.65f + r*0.18f);
        });
        binding.imageSlider.setPageTransformer(transformer);
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

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000);
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request the user to grant location permission
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // Create a LocationRequest object
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY);
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
                Toast.makeText(requireContext(), "Location permission is required.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}