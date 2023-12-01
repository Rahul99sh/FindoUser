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
import com.google.firebase.Timestamp;
import com.users.findo.R;
import com.users.findo.activities.AllStores;
import com.users.findo.activities.FilterItems;
import com.users.findo.activities.ItemsDetails;
import com.users.findo.activities.StoreDetails;
import com.users.findo.adapters.FeaturedItemAdapter;
import com.users.findo.adapters.SliderAdapter;
import com.users.findo.adapters.StoreListRvAdapter;
import com.users.findo.adapters.allItemsRvAdapter;
import com.users.findo.dataClasses.CartDatabase;
import com.users.findo.dataClasses.Item;
import com.users.findo.dataClasses.Promotion;
import com.users.findo.dataClasses.Store;
import com.users.findo.databaseClass.CartDb;
import com.users.findo.databaseClass.FavDb;
import com.users.findo.databinding.FragmentDashboardBinding;
import com.users.findo.viewModels.PromotionViewModel;
import com.users.findo.viewModels.StoreViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;


public class DashboardFragment extends Fragment {


    private FusedLocationProviderClient mFusedLocationClient;
    public Geocoder geocoder;
    private LocationCallback mLocationCallback;
    public static String state, city;
    List<Item>featuredItems, newItems;
    public static List<Item>allItems;
    private Handler handler;
    FragmentDashboardBinding binding;
    public static double userLat,userLong;
    PromotionViewModel promotionViewModel;
    StoreViewModel storeViewModel;
    FeaturedItemAdapter featuredItemAdapter;
    allItemsRvAdapter itemListAdapter;
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
            //get All Items of above items
            storeViewModel.getLiveStoreItemsData().observe(getViewLifecycleOwner(), items -> {
                this.allItems = items;
                getFeaturedItem(items);
                featuredItemAdapter = new FeaturedItemAdapter(requireContext(), featuredItems, new FeaturedItemAdapter.FeaturedItemListener() {
                    @Override
                    public void FavItemOnClick(FeaturedItemAdapter.MyViewHolder v, int position) {

                        FavDb favDb = new FavDb(requireContext());
                        if(favDb.itemExist(featuredItems.get(position).getItemId())){
                            //item is already in fav list -> remove it
                            favDb.deleteItem(featuredItems.get(position).getItemId());
                            v.favImage.setImageResource(R.drawable.ic_white_heart);
                            //notify data set changed
                        }else{
                            //add into favDb
                            favDb.insertOneItem(newItems.get(position));
                            v.favImage.setVisibility(View.GONE);
                            v.heart.setAnimation(R.raw.heart);
                            v.heart.setScaleX(1.45f);
                            v.heart.setScaleY(1.45f);
                            v.heart.setSpeed(2.5f);
                            v.heart.playAnimation();
                        }
                    }

                    @Override
                    public void AddToCartItemOnClick(FeaturedItemAdapter.MyViewHolder v, int position) {

                        CartDb cartDb = new CartDb(requireContext());
                        cartDb.insertOneItem(featuredItems.get(position));
                        v.sparkle.setAnimation(R.raw.sparkle);
                        v.sparkle.setScaleX(3f);
                        v.sparkle.setScaleY(3f);
                        v.sparkle.playAnimation();
                        v.addToCart.setVisibility(View.GONE);
                        v.removeFromCart.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void RemoveFromCartItemOnClick(FeaturedItemAdapter.MyViewHolder v, int position) {

                        CartDb cartDb = new CartDb(requireContext());
                        cartDb.deleteItem(featuredItems.get(position).getItemId());
                        v.addToCart.setVisibility(View.VISIBLE);
                        v.removeFromCart.setVisibility(View.GONE);
                    }

                    @Override
                    public void ItemOnClick(FeaturedItemAdapter.MyViewHolder v, int position) {

                        Intent intent = new Intent(getActivity(), ItemsDetails.class);
                        intent.putExtra("item",featuredItems.get(position));
                        startActivity(intent);
                    }
                });
                itemListAdapter = new allItemsRvAdapter(requireContext(), newItems, new allItemsRvAdapter.AllItemListener() {
                    @Override
                    public void FavItemOnClick(allItemsRvAdapter.MyViewHolder1 v, int position) {
                        FavDb favDb = new FavDb(requireContext());
                        if(favDb.itemExist(newItems.get(position).getItemId())){
                            //item is already in fav list -> remove it
                            favDb.deleteItem(newItems.get(position).getItemId());
                            v.favImage.setImageResource(R.drawable.ic_white_heart);
                            //notify data set changed
                        }else{
                            //add into favDb
                            favDb.insertOneItem(newItems.get(position));
                            v.favImage.setVisibility(View.GONE);
                            v.heart.setAnimation(R.raw.heart);
                            v.heart.setScaleX(1.45f);
                            v.heart.setScaleY(1.45f);
                            v.heart.setSpeed(2.5f);
                            v.heart.playAnimation();
                        }
                    }

                    @Override
                    public void AddToCartItemOnClick(allItemsRvAdapter.MyViewHolder1 v, int position) {
                        CartDb cartDb = new CartDb(requireContext());
                        cartDb.insertOneItem(newItems.get(position));
                        v.sparkle.setAnimation(R.raw.sparkle);
                        v.sparkle.setScaleX(3f);
                        v.sparkle.setScaleY(3f);
                        v.sparkle.playAnimation();
                        v.addToCart.setVisibility(View.GONE);
                        v.removeFromCart.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void RemoveFromCartItemOnClick(allItemsRvAdapter.MyViewHolder1 v, int position) {
                        CartDb cartDb = new CartDb(requireContext());
                        cartDb.deleteItem(newItems.get(position).getItemId());
                        v.addToCart.setVisibility(View.VISIBLE);
                        v.removeFromCart.setVisibility(View.GONE);
                    }

                    @Override
                    public void ItemOnClick(allItemsRvAdapter.MyViewHolder1 v, int position) {
                        Intent intent = new Intent(getActivity(), ItemsDetails.class);
                        intent.putExtra("item",newItems.get(position));
                        startActivity(intent);
                    }
                });
                binding.allItemsRv.setAdapter(itemListAdapter);
                binding.featuredRv.setAdapter(featuredItemAdapter);
            });
            binding.mainView.setVisibility(View.VISIBLE);
            stores.sort((store1, store2) -> Float.compare(store1.getDist(), store2.getDist()));
            List<Store> top5Stores = stores.subList(0, Math.min(stores.size(), 5));
            storeListRvAdapter = new StoreListRvAdapter(requireContext(), top5Stores, new StoreListRvAdapter.ItemClickListener() {
                @Override
                public void itemOnClickListener(View v, int position) {
                    Intent i = new Intent(requireContext(), StoreDetails.class);
                    i.putExtra("store",top5Stores.get(position));
                    startActivity(i);
                }
            });
            binding.storeRv.setAdapter(storeListRvAdapter);
        });

    }
    private void getFeaturedItem(List<Item> items){
        featuredItems.clear();
        newItems.clear();
        for (Item i :
                items) {
            if(i!=null) {
                if (i.getItemTag().equals("Featured")) {
                    featuredItems.add(i);
                } else if (i.getItemTag().equals("New")) {
                    newItems.add(i);
                }
            }
        }
    }
    private void setupSlideBar(List<Promotion> promotions) {
        List<String> imageList = new ArrayList<>();
        Timestamp currentTimestamp = Timestamp.now(); // Get current timestamp
        for (Promotion promo : promotions) {
            Timestamp endDate = promo.getEndDate(); // Assuming you have a method to get the end date

            if (endDate != null && endDate.compareTo(currentTimestamp) > 0) {
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
                    },3000);
                }else{
                    handler.postDelayed(runnable, 3000);
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