package com.users.findo.activities;

import static com.users.findo.fragments.DashboardFragment.allItems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
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
import com.users.findo.viewModels.CategoryViewModel;
import com.users.findo.viewModels.GroceryCategoryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FilterItems extends AppCompatActivity {

    ArrayList<String> searchHistory = new ArrayList<>();
    SearchView searchView;
    RelativeLayout searchHistoryLay;
    public RecyclerView categoryFilter;
    ArrayList<Item> filteredItemList = new ArrayList<>();
    ArrayList<Item> filteredItemList1 = new ArrayList<>();
    CategoryViewModel categoryViewModel;
    GroceryCategoryViewModel groceryCategoryViewModel;
    List<Category> categories2 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_items);
        RecyclerView searchRv = findViewById(R.id.searchHistoryRv);
        RecyclerView filterItems = findViewById(R.id.filter_item_rv);
        categoryFilter = findViewById(R.id.categoryViseFilter);
        LinearLayout category = findViewById(R.id.category);
        SearchHistoryDb searchHistoryDb = new SearchHistoryDb(this);
        searchHistory = searchHistoryDb.getData();
        groceryCategoryViewModel = new ViewModelProvider(this).get(GroceryCategoryViewModel.class);
        searchView = findViewById(R.id.Filter_searchView);
        searchHistoryLay = findViewById(R.id.searchHistoryLay);
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
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getCategoryLiveData().observe(this, categories -> {
            categories2.addAll(categories);
            groceryCategoryViewModel.getCategoryLiveData().observe(this, categories1 -> {
                categories2.addAll(categories1);
                CategoryFilters adapter2 = new CategoryFilters(FilterItems.this, categories2, new CategoryFilters.itemClick() {
                    @Override
                    public void onItemClick(View v, int pos, int selpos) {
                        if (selpos == -1) {
                            v.setBackgroundResource(R.drawable.grad_nav_g);
                            filteredItemList1 = filterItems(categories2.get(pos).getName());
                            adapter1.setFilteredItemList(filteredItemList1);
                        } else {
                            if (selpos != pos) {
                                // When a different item is clicked
                                RecyclerView.ViewHolder prevViewHolder = categoryFilter.findViewHolderForAdapterPosition(selpos);
                                if (prevViewHolder != null) {
                                    prevViewHolder.itemView.findViewById(R.id.back).setBackgroundResource(R.drawable.nav_grad_f);
                                }
                                v.setBackgroundResource(R.drawable.grad_nav_g);
                                filteredItemList1 = filterItems(categories2.get(pos).getName());
                                adapter1.setFilteredItemList(filteredItemList1);
                            } else {
                                // When the same item is clicked again
                                v.setBackgroundResource(R.drawable.nav_grad_f);
                                filteredItemList1 = filterItems("");
                                adapter1.setFilteredItemList(filteredItemList1);
                            }
                        }
                    }
                });
                categoryFilter.setAdapter(adapter2);
            });

        });




        SearchHistoryAdapter adapter = new SearchHistoryAdapter(this,searchHistory);

        searchRv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        searchRv.setAdapter(adapter);



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
            if(item.getItemName()!=null && item.getCategory()!=null && (item.getItemName().toLowerCase(Locale.ROOT).contains(q.toLowerCase(Locale.ROOT)) || item.getCategory().toLowerCase(Locale.ROOT).contains(q.toLowerCase(Locale.ROOT))
                    || item.getStoreName().toLowerCase(Locale.ROOT).contains(q.toLowerCase(Locale.ROOT)))){
                filteredItemList.add(item);
            }
        }
        //update filtered list

        return filteredItemList;

    }

    private ArrayList<Item> filterItemsCatrgory(String q){

        List<Item> items = allItems;

        filteredItemList.clear();
        for(Item item : items){
            if( item.getCategory().toLowerCase(Locale.ROOT).contains(q.toLowerCase(Locale.ROOT))
                    ){
                filteredItemList.add(item);
            }
        }
        return filteredItemList;

    }

}