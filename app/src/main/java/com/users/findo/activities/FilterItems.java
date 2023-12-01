package com.users.findo.activities;

import static com.users.findo.fragments.DashboardFragment.allItems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.users.findo.R;
import com.users.findo.adapters.CategoryFilters;
import com.users.findo.adapters.FilterItemAdapter;
import com.users.findo.adapters.SearchHistoryAdapter;
import com.users.findo.dataClasses.Category;
import com.users.findo.dataClasses.Item;
import com.users.findo.databaseClass.CartDb;
import com.users.findo.databaseClass.SearchHistoryDb;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FilterItems extends AppCompatActivity {

    ArrayList<String> searchHistory = new ArrayList<>();
    SearchView searchView;
    RelativeLayout searchHistoryLay;
    ArrayList<Item> filteredItemList = new ArrayList<>();
    ArrayList<Item> filteredItemList1 = new ArrayList<>();

    ArrayList<Category> categories = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_items);

        SearchHistoryDb searchHistoryDb = new SearchHistoryDb(this);
        searchHistory = searchHistoryDb.getData();

        searchView = findViewById(R.id.Filter_searchView);
        searchHistoryLay = findViewById(R.id.searchHistoryLay);

        RecyclerView searchRv = findViewById(R.id.searchHistoryRv);
        RecyclerView filterItems = findViewById(R.id.filter_item_rv);
        RecyclerView categoryFilter = findViewById(R.id.categoryViseFilter);
        LinearLayout category = findViewById(R.id.category);

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

        CategoryFilters adapter2 = new CategoryFilters(FilterItems.this,categories);
//        GridLayoutManager layoutManager = new GridLayoutManager(FilterItems.this,2);
//        categoryFilter.setLayoutManager(layoutManager);
        categoryFilter.setAdapter(adapter2);

        SearchHistoryAdapter adapter = new SearchHistoryAdapter(this,searchHistory);

        searchRv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        searchRv.setAdapter(adapter);

        FilterItemAdapter adapter1 = new FilterItemAdapter(this, new ArrayList<>(), new FilterItemAdapter.ItemClickListener() {
            @Override
            public void AddToCartItemOnClick(FilterItemAdapter.MyNewViewHolder v, int position) {
                CartDb cartDb = new CartDb(FilterItems.this);
                cartDb.insertOneItem(filteredItemList.get(position));
                v.addToCart.setVisibility(View.GONE);
                v.removeFromCart.setVisibility(View.VISIBLE);
            }

            @Override
            public void RemoveFromCartItemOnClick(FilterItemAdapter.MyNewViewHolder v, int position) {
                CartDb cartDb = new CartDb(FilterItems.this);
                cartDb.deleteItem(filteredItemList.get(position).getItemId());
                v.addToCart.setVisibility(View.VISIBLE);
                v.removeFromCart.setVisibility(View.GONE);
            }

            @Override
            public void ItemOnClick(FilterItemAdapter.MyNewViewHolder v, int position) {
                Intent intent = new Intent(FilterItems.this,ItemsDetails.class);
                intent.putExtra("item",filteredItemList.get(position));
                startActivity(intent);
            }
        });

        filterItems.setLayoutManager(new GridLayoutManager(FilterItems.this,2));
        filterItems.setAdapter(adapter1);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchHistoryDb db = new SearchHistoryDb(FilterItems.this);
                filteredItemList1 = filterItems(query);
                adapter1.setFilteredItemList(filteredItemList1);
                adapter1.notifyDataSetChanged();
                searchRv.setVisibility(View.GONE);
                if(filteredItemList1.isEmpty() || query.length() == 0){
                    // hide this Rv
                    filterItems.setVisibility(View.GONE);
                    searchHistoryLay.setVisibility(View.VISIBLE);
                    category.setVisibility(View.GONE);
                }else{
                    db.insertData(query);
                    filterItems.setVisibility(View.VISIBLE);
                    searchHistoryLay.setVisibility(View.GONE);
                    category.setVisibility(View.VISIBLE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Item> filteredItemList;
                filteredItemList = filterItems(newText);
                adapter1.setFilteredItemList(filteredItemList);
                adapter1.notifyDataSetChanged();
                searchRv.setVisibility(View.GONE);
                if(filteredItemList.isEmpty() || newText.length() == 0){
                    // hide this Rv
                    filterItems.setVisibility(View.GONE);
                    searchHistoryLay.setVisibility(View.VISIBLE);
                    category.setVisibility(View.GONE);
                }else{
                    filterItems.setVisibility(View.VISIBLE);
                    searchHistoryLay.setVisibility(View.GONE);
                    category.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

    }

    //func to filter items
    private ArrayList<Item> filterItems(String q){

        List<Item> items = allItems;

        filteredItemList.clear();
        for(Item item : items){
            if(item.getItemName().toLowerCase(Locale.ROOT).contains(q.toLowerCase(Locale.ROOT)) || item.getCategory().toLowerCase(Locale.ROOT).contains(q.toLowerCase(Locale.ROOT))
                    || item.getStoreName().toLowerCase(Locale.ROOT).contains(q.toLowerCase(Locale.ROOT))){
                filteredItemList.add(item);
            }
        }
        //update filtered list

        return filteredItemList;

    }


}