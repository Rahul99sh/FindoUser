package com.users.findo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.users.findo.dataClasses.Category;
import com.users.findo.R;
import com.users.findo.adapters.CategoryFragAdapter;
import com.users.findo.activities.FilterItems;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {


    public static ArrayList<Category> categories = new ArrayList<>();
    private RecyclerView categoryRv;
    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        categoryRv = view.findViewById(R.id.categoriesFragRV);
        categories.clear();

        categories.add(new Category("Fruits & vegetables",R.drawable.category_fruit,R.color.transparent_green,R.color.featured));
        categories.add(new Category("Diary, Bread &\n Eggs",R.drawable.category_eggs_bread,R.color.transparent_yellow,R.color.yellow));
        categories.add(new Category("Beverages",R.drawable.category_beverage,R.color.DarkYellow,R.color.SecondYellow));
        categories.add(new Category("Instant Food",R.drawable.category_instant_food,R.color.transparent_Purple,R.color.purple));
        categories.add(new Category("Cleaning &\n Household",R.drawable.category_household,R.color.transparent_red,R.color.red));
        categories.add(new Category("Personal Care",R.drawable.category_personal_care,R.color.transparent_flamingo,R.color.flamingo));
        categories.add(new Category("Baby Care",R.drawable.category_baby_care,R.color.transparent_cream,R.color.cream));
        categories.add(new Category("Pet Care",R.drawable.category_pet_care,R.color.transparent_magenta,R.color.magenta));
        categories.add(new Category("Party Essentials",R.drawable.category_party_essentials,R.color.transparent_cyan,R.color.cyan));
        categories.add(new Category("Desserts",R.drawable.category_desserts,R.color.transparent_mustard,R.color.mustard));
        categories.add(new Category("Munchies",R.drawable.category_munchies,R.color.transparent_Denim,R.color.transparent_Denim));
        categories.add(new Category("Stationary",R.drawable.category_stationary,R.color.transparent_cherub,R.color.transparent_cherub));
        categories.add(new Category("Utensils",R.drawable.category_utensils,R.color.transparent_darkBrown,R.color.darkBrown));

        CategoryFragAdapter adapter = new CategoryFragAdapter(categories);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(),2);
        categoryRv.setLayoutManager(layoutManager);
        categoryRv.setAdapter(adapter);

        CardView SearchBox = view.findViewById(R.id.SearchBox);
        SearchBox.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), FilterItems.class);
            startActivity(intent);
        });


        return view;
    }
}