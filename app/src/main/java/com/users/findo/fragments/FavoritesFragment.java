package com.users.findo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.users.findo.dataClasses.CartDatabase;
import com.users.findo.databaseClass.CartDb;
import com.users.findo.databaseClass.FavDb;
import com.users.findo.R;
import com.users.findo.adapters.ItemListAdapter;
import com.users.findo.activities.FilterItems;

import java.util.ArrayList;


public class FavoritesFragment extends Fragment {

    ArrayList<CartDatabase> favList = new ArrayList<>();

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        FavDb favDb = new FavDb(requireContext());
        favList = favDb.getData();

        Button startSearch = view.findViewById(R.id.explore_now);
        LinearLayout favEmpty = view.findViewById(R.id.favEmpty);
        RecyclerView favRv = view.findViewById(R.id.favRv);
        TextView favtext = view.findViewById(R.id.favtext);

        CardView SearchBox = view.findViewById(R.id.SearchBox);
        SearchBox.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), FilterItems.class);
            startActivity(intent);
        });

//        favRv.setLayoutManager(new GridLayoutManager(requireContext(),2));

        ItemListAdapter adapter = new ItemListAdapter(requireContext(), favList, new ItemListAdapter.ItemClickListener() {
            @Override
            public void FavItemOnClick(ItemListAdapter.MyViewHolder v, int position) {
                FavDb favDb = new FavDb(requireContext());
                if(favDb.itemExist(favList.get(position).getItemId())){
                    //item is already in fav list -> remove it
                    favDb.deleteItem(favList.get(position).getItemId());
                    favList.remove(position);
                    //notify data set changed
                    v.favImage.setImageResource(R.drawable.ic_white_heart);
                    if(favList.isEmpty()){
                        favRv.setVisibility(View.GONE);
                        favEmpty.setVisibility(View.VISIBLE);
                        startSearch.setVisibility(View.VISIBLE);
                        favtext.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void AddToCartItemOnClick(ItemListAdapter.MyViewHolder v, int position) {
                CartDb cartDb = new CartDb(requireContext());
                if(!cartDb.itemExist(favList.get(position).getItemId())){
                    cartDb.insertOneItem(new CartDatabase(favList.get(position).getStoreId(),favList.get(position).getItemId()
                            ,favList.get(position).getItemName(),favList.get(position).getStoreName(),favList.get(position).getStoreUrl(),favList.get(position).getItemUrl()
                            ,favList.get(position).getStoreLat(),favList.get(position).getStoreLong(), favList.get(position).getItemCategory(), favList.get(position).getItemTag(), favList.get(position).getPrice()));
                    v.addToCart.setVisibility(View.GONE);
                    v.removeFromCart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void RemoveFromCartItemOnClick(ItemListAdapter.MyViewHolder v, int position) {
                CartDb cartDb = new CartDb(requireContext());
                if(cartDb.itemExist(favList.get(position).getItemId())) {
                    cartDb.deleteItem(favList.get(position).getItemId());
                    v.addToCart.setVisibility(View.VISIBLE);
                    v.removeFromCart.setVisibility(View.GONE);
                }
            }

            @Override
            public void ItemOnClick(ItemListAdapter.MyViewHolder v, int position) {

            }
        });
        favRv.setAdapter(adapter);

        if(!favList.isEmpty()){
            favRv.setVisibility(View.VISIBLE);
            favEmpty.setVisibility(View.GONE);
            startSearch.setVisibility(View.GONE);
            favtext.setVisibility(View.VISIBLE);
        }else{
            favRv.setVisibility(View.GONE);
            favEmpty.setVisibility(View.VISIBLE);
            startSearch.setVisibility(View.VISIBLE);
            favtext.setVisibility(View.GONE);
        }
        return view;
    }
}