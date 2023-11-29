package com.users.findo.fragments;


import static com.users.findo.MainActivity.chipNavigationBar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.users.findo.dataClasses.CartDatabase;
import com.users.findo.dataClasses.Category;
import com.users.findo.dataClasses.Store;
import com.users.findo.databaseClass.CartDb;
import com.users.findo.databaseClass.FavDb;
import com.users.findo.R;
import com.users.findo.adapters.CategoryAdapter;
import com.users.findo.adapters.FeaturedItemAdapter;
import com.users.findo.adapters.StoreListRvAdapter;
import com.users.findo.adapters.allItemsRvAdapter;
import com.users.findo.activities.AllStores;
import com.users.findo.activities.FilterItems;
import com.users.findo.activities.ItemsDetails;
import com.users.findo.activities.StoreDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;


public class DashboardFragment extends Fragment {


    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public DashboardFragment(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater localInflater = inflater.cloneInContext(mContext);
        View view = localInflater.inflate(R.layout.fragment_dashboard, container, false);

//        storeList.clear();
//        itemListName.clear();
//        storeListRv = view.findViewById(R.id.storeRv);
//        allItems = view.findViewById(R.id.allItemsRv);
//        featuredRv = view.findViewById(R.id.featuredRv);
//        register = view.findViewById(R.id.slider);
//        scrollView = view.findViewById(R.id.scroll_view);
//        RecyclerView categoryRv = view.findViewById(R.id.categoryRv);
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
//
//        seeAllStores = view.findViewById(R.id.seeALLStore);
//        seeAllCate = view.findViewById(R.id.seeMoreCategories);
//        shimmerFrameLayout = view.findViewById(R.id.shimmer_frag);
//        shimmerFrameLayout.startShimmer();
//
//        seeAllStores.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getActivity(), AllStores.class);
//                startActivity(i);
//            }
//        });
//        mLocationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(@NonNull LocationResult locationResult) {
//                Location location = locationResult.getLastLocation();
//                if (location != null) {
//                    double latitude = location.getLatitude();
//                    double longitude = location.getLongitude();
//                    Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
//                    List<Address> addresses = null;
//                    try {
//                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
//                        if (addresses != null && addresses.size() > 0) {
//                            Address address = addresses.get(0);
//                            String area = address.getSubLocality();
//                            String city = address.getLocality();
//
//                            String loc = area + ", " + city;
//
//                            TextView locationArea = view.findViewById(R.id.location);
//                            locationArea.setText(loc);
//                        }
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    rootRef.collection("Store")
//                            .get()
//                            .addOnCompleteListener(task -> {
//                                if (task.isSuccessful()){
//                                    //find curr loc and calc dist b/w you and store and sort list
//                                    Location location1 = new Location("");
//                                    location1.setLatitude(latitude);
//                                    location1.setLongitude(longitude);
//                                    storeList.clear();
//                                    itemListName.clear();
//                                    shimmerFrameLayout.stopShimmer();
//                                    shimmerFrameLayout.setVisibility(View.GONE);
//                                    scrollView.setVisibility(View.VISIBLE);
//
//
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                        Location location2 = new Location("");
//                                        double lat = Objects.requireNonNull(document.getDouble("StoreLat"));
//                                        double long1 = Objects.requireNonNull(document.getDouble("StoreLong"));
//                                        location2.setLatitude(lat);
//                                        location2.setLongitude(long1);
//                                    Geocoder geocoder1 = new Geocoder(requireContext(), Locale.getDefault());
//                                    List<Address> addresses1 = null;
//                                    String loc="";
//                                    try {
//                                        addresses1 = geocoder1.getFromLocation(lat, long1, 1);
//                                        if (addresses1 != null && addresses1.size() > 0) {
//                                            Address address = addresses1.get(0);
//                                            loc = address.getSubLocality();
//                                        }
//                                    } catch (IOException e) {
//                                        throw new RuntimeException(e);
//                                    }
//                                        float dist = location1.distanceTo(location2) / 1000.0f;
//                                        String distance = loc + ", " +String.format("%.2f", dist) + " km";
//                                        Store store = new Store(document.getString("StoreName"),
//                                                document.getDouble("StoreLat"),
//                                                document.getDouble("StoreLong"),
//                                                document.getString("StoreUrl"),
//                                                distance,dist,document.getString("StoreId"));
//                                        storeList.add(store);
//                                    }
//                                    // Define a custom Comparator to sort the MyObject instances by ID
//                                    Comparator<Store> comparator = new Comparator<Store>() {
//                                        @Override
//                                        public int compare(Store o1, Store o2) {
//                                            return Integer.compare(Float.valueOf(o1.getDist()*100).intValue(), Float.valueOf(o2.getDist()*100).intValue());
//                                        }
//                                    };
//
//                                    // Sort the ArrayList using the custom Comparator
//                                    storeList.sort(comparator);
//                                    ArrayList<Store> fiveList = new ArrayList<>();
//                                    for(int i =0; i<5 ;i++){
//                                        fiveList.add(storeList.get(i));
//                                    }
//                                    StoreListRvAdapter adapter = new StoreListRvAdapter(requireContext(), fiveList, (v, position) -> {
//                                        Intent intent = new Intent(requireContext(), StoreDetails.class);
//                                        intent.putExtra("storeId", storeList.get(position).getStoreId());
//                                        startActivity(intent);
//                                    });
//                                    storeListRv.setAdapter(adapter);
//                                }
//                            }).addOnFailureListener(e -> {
//                            });
//
//                }
//            }
//        };
//
//        CardView SearchBox = view.findViewById(R.id.SearchBox);
//        SearchBox.setOnClickListener(view1 -> {
//            Intent intent = new Intent(requireContext(), FilterItems.class);
//            startActivity(intent);
//        });
//
//        rootRef.collection("Store")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        //find curr loc and calc dist b/w you and store and sort list
//                        featuredItems.clear();
//                        Set<String> itemIds = new HashSet<>();
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//
//
//                            itemRef.collection("Store").document(Objects.requireNonNull(document.getString("StoreId")))
//                                    .collection("Items").get().addOnCompleteListener(task1 -> {
//                                        for (QueryDocumentSnapshot doc : task1.getResult()) {
//                                            String itemId = doc.getString("ItemId");
//                                            CartDatabase cartDatabase = new CartDatabase(document.getString("StoreId"),
//                                                    doc.getString("ItemId"), doc.getString("ItemName"), document.getString("StoreName")
//                                                    , document.getString("StoreUrl"), doc.getString("ItemUrl"), document.getDouble("StoreLat"),
//                                                    document.getDouble("StoreLong"), doc.getString("Category"), doc.getString("ItemTag"), doc.getString("price"));
//                                            if (!itemIds.contains(itemId) && Objects.equals(doc.getString("ItemTag"), "Featured")) {
//                                                featuredItems.add(cartDatabase);
//                                                itemIds.add(itemId);
//                                            }
//                                        }
//
//                                        featuredRv.setAdapter(new FeaturedItemAdapter(requireContext(), featuredItems, new FeaturedItemAdapter.FeaturedItemListener() {
//                                            @Override
//                                            public void FavItemOnClick(FeaturedItemAdapter.MyViewHolder v, int position) {
//
//                                                FavDb favDb = new FavDb(requireContext());
//                                                if(favDb.itemExist(featuredItems.get(position).getItemId())){
//                                                    //item is already in fav list -> remove it
//                                                    favDb.deleteItem(featuredItems.get(position).getItemId());
//                                                    v.favImage.setImageResource(R.drawable.ic_white_heart);
//                                                    //notify data set changed
//                                                }else{
//                                                    //add into favDb
//                                                    favDb.insertOneItem(new CartDatabase(featuredItems.get(position).getStoreId(), featuredItems.get(position).getItemId()
//                                                            , featuredItems.get(position).getItemName(), featuredItems.get(position).getStoreName(), featuredItems.get(position).getStoreUrl(), featuredItems.get(position).getItemUrl()
//                                                            , featuredItems.get(position).getStoreLat(), featuredItems.get(position).getStoreLong(),featuredItems.get(position).getItemCategory(),featuredItems.get(position).getItemTag(),featuredItems.get(position).getPrice()));
//                                                    v.favImage.setVisibility(View.GONE);
//                                                    v.heart.setAnimation(R.raw.heart);
//                                                    v.heart.setScaleX(1.45f);
//                                                    v.heart.setScaleY(1.45f);
//                                                    v.heart.setSpeed(2.5f);
//                                                    v.heart.playAnimation();
//                                                }
//                                            }
//
//                                            @Override
//                                            public void AddToCartItemOnClick(FeaturedItemAdapter.MyViewHolder v, int position) {
//
//                                                CartDb cartDb = new CartDb(requireContext());
//                                                cartDb.insertOneItem(featuredItems.get(position));
//                                                v.sparkle.setAnimation(R.raw.sparkle);
//                                                v.sparkle.setScaleX(3f);
//                                                v.sparkle.setScaleY(3f);
//                                                v.sparkle.playAnimation();
//                                                v.addToCart.setVisibility(View.GONE);
//                                                v.removeFromCart.setVisibility(View.VISIBLE);
//                                            }
//
//                                            @Override
//                                            public void RemoveFromCartItemOnClick(FeaturedItemAdapter.MyViewHolder v, int position) {
//
//                                                CartDb cartDb = new CartDb(requireContext());
//                                                cartDb.deleteItem(featuredItems.get(position).getItemId());
//                                                v.addToCart.setVisibility(View.VISIBLE);
//                                                v.removeFromCart.setVisibility(View.GONE);
//                                            }
//
//                                            @Override
//                                            public void ItemOnClick(FeaturedItemAdapter.MyViewHolder v, int position) {
//
//                                                Intent intent = new Intent(getActivity(), ItemsDetails.class);
//                                                intent.putExtra("itemId",featuredItems.get(position).getItemId());
//                                                intent.putExtra("itemCategory",featuredItems.get(position).getItemCategory());
//                                                intent.putExtra("storeId",featuredItems.get(position).getStoreId());
//                                                startActivity(intent);
//                                            }
//                                        }));
//
//                                    }).addOnFailureListener(e -> {
//
//                                    });
//                        }
//                    }
//                });
//
////        allItems.setLayoutManager(new GridLayoutManager(requireContext(),2));
//        rootRef.collection("Store")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        //find curr loc and calc dist b/w you and store and sort list
//                        featuredItems.clear();
//                        AllItems.clear();
//                        Set<String> itemIds = new HashSet<>();
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//
//
//                            itemRef.collection("Store").document(Objects.requireNonNull(document.getString("StoreId")))
//                                    .collection("Items").get().addOnCompleteListener(task1 -> {
//                                        for (QueryDocumentSnapshot doc : task1.getResult()) {
//                                            String itemId = doc.getString("ItemId");
//                                            CartDatabase cartDatabase = new CartDatabase(document.getString("StoreId"),
//                                                    doc.getString("ItemId"), doc.getString("ItemName"), document.getString("StoreName")
//                                                    , document.getString("StoreUrl"), doc.getString("ItemUrl"), document.getDouble("StoreLat"),
//                                                    document.getDouble("StoreLong"), doc.getString("Category"), doc.getString("ItemTag"), doc.getString("price"));
//                                            AllItems.add(cartDatabase);
//
//                                        }
//                                        allItems.setAdapter(new allItemsRvAdapter(requireContext(), AllItems, new allItemsRvAdapter.AllItemListener() {
//                                            @Override
//                                            public void FavItemOnClick(allItemsRvAdapter.MyViewHolder1 v, int position) {
//                                                FavDb favDb = new FavDb(requireContext());
//                                                if(favDb.itemExist(AllItems.get(position).getItemId())){
//                                                    //item is already in fav list -> remove it
//                                                    favDb.deleteItem(AllItems.get(position).getItemId());
//                                                    v.favImage.setImageResource(R.drawable.ic_white_heart);
//                                                    //notify data set changed
//                                                }else{
//                                                    //add into favDb
//                                                    favDb.insertOneItem(new CartDatabase(AllItems.get(position).getStoreId(), AllItems.get(position).getItemId()
//                                                            , AllItems.get(position).getItemName(), AllItems.get(position).getStoreName(), AllItems.get(position).getStoreUrl(), AllItems.get(position).getItemUrl()
//                                                            , AllItems.get(position).getStoreLat(), AllItems.get(position).getStoreLong(),AllItems.get(position).getItemCategory(),AllItems.get(position).getItemTag(),AllItems.get(position).getPrice()));
//                                                    v.favImage.setVisibility(View.GONE);
//                                                    v.heart.setAnimation(R.raw.heart);
//                                                    v.heart.setScaleX(1.45f);
//                                                    v.heart.setScaleY(1.45f);
//                                                    v.heart.setSpeed(2.5f);
//                                                    v.heart.playAnimation();
//                                                }
//                                            }
//
//                                            @Override
//                                            public void AddToCartItemOnClick(allItemsRvAdapter.MyViewHolder1 v, int position) {
//                                                CartDb cartDb = new CartDb(requireContext());
//                                                cartDb.insertOneItem(AllItems.get(position));
//                                                v.sparkle.setAnimation(R.raw.sparkle);
//                                                v.sparkle.setScaleX(3f);
//                                                v.sparkle.setScaleY(3f);
//                                                v.sparkle.playAnimation();
//                                                v.addToCart.setVisibility(View.GONE);
//                                                v.removeFromCart.setVisibility(View.VISIBLE);
//                                            }
//
//                                            @Override
//                                            public void RemoveFromCartItemOnClick(allItemsRvAdapter.MyViewHolder1 v, int position) {
//                                                CartDb cartDb = new CartDb(requireContext());
//                                                cartDb.deleteItem(AllItems.get(position).getItemId());
//                                                v.addToCart.setVisibility(View.VISIBLE);
//                                                v.removeFromCart.setVisibility(View.GONE);
//                                            }
//
//                                            @Override
//                                            public void ItemOnClick(allItemsRvAdapter.MyViewHolder1 v, int position) {
//                                                Intent intent = new Intent(getActivity(), ItemsDetails.class);
//                                                intent.putExtra("itemId",AllItems.get(position).getItemId());
//                                                intent.putExtra("itemCategory",AllItems.get(position).getItemCategory());
//                                                intent.putExtra("storeId",AllItems.get(position).getStoreId());
//                                                startActivity(intent);
//                                            }
//                                        }));
//
//
//                                    }).addOnFailureListener(e -> {
//
//                                    });
//                        }
//                    }
//                });
//        //?
//
//        storeListRv.setHasFixedSize(true);
//        featuredRv.setHasFixedSize(true);
//
//
//        SliderView sliderView = view.findViewById(R.id.SliderStore);
//
//        ImageSlider imageAdapter = new ImageSlider(requireContext());
//
//        sliderView.setSliderAdapter(imageAdapter);
//
//        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//        sliderView.setIndicatorSelectedColor(Color.WHITE);
//        sliderView.setIndicatorUnselectedColor(Color.GRAY);
//        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
//        sliderView.startAutoCycle();
//
//
//        promoRef.collection("Promotions").get().addOnCompleteListener(task -> {
//            for (QueryDocumentSnapshot document : task.getResult()){
//                SliderItem promotion = new SliderItem(document.getString("PromoId"),document.getString("ImageUrl"));
//                promos.add(promotion);
//                imageAdapter.addItem(promotion);
//            }
//        }).addOnFailureListener(e -> {
//
//        });
//        ArrayList<Category> categories = new ArrayList<>();
//
//        //adding categories in dashboard
//        categories.add(new Category("Fruits & vegetables",R.drawable.category_fruit,R.color.transparent_green,R.color.featured));
//        categories.add(new Category("Diary, Bread &\n Eggs",R.drawable.category_eggs_bread,R.color.transparent_yellow,R.color.yellow));
//        categories.add(new Category("Beverages",R.drawable.category_beverage,R.color.DarkYellow,R.color.SecondYellow));
//        categories.add(new Category("Instant Food",R.drawable.category_instant_food,R.color.transparent_Purple,R.color.purple));
//
//        //setting recycler view adapter
//        CategoryAdapter adapter = new CategoryAdapter(requireContext(),categories);
//        categoryRv.setAdapter(adapter);
//
//        //switch to category fragment when see all category is clicked
//        seeAllCate.setOnClickListener(v -> {
//            change_Activity(new CategoriesFragment());
//            chipNavigationBar.setItemSelected(R.id.activity,true);
//        });

        return view;
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
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request the user to grant location permission
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
                Toast.makeText(requireContext(), "Location permission is required.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void change_Activity(Fragment fragment){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

    private Context mContext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

}