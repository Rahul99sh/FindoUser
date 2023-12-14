package com.users.findo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.users.findo.dataClasses.CartDatabase;
import com.users.findo.dataClasses.Item;
import com.users.findo.databaseClass.CartDb;
import com.users.findo.R;
import com.users.findo.adapters.CartListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private List<Item> cartLists = new ArrayList<>();
    private RecyclerView cartRv;

    private LinearLayout  cartEmpty;

    private TextView carttext;
    private Button startSearch;
    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        startSearch = view.findViewById(R.id.startSearch);
        cartEmpty =  view.findViewById(R.id.cartempty);
        cartRv =  view.findViewById(R.id.cartRv);
        carttext =  view.findViewById(R.id.carttext);


        CartDb cartDb = new CartDb(requireContext());
        cartLists = cartDb.getData();
        CartListAdapter adapter = new CartListAdapter(requireContext(),cartLists, position -> {
            cartDb.deleteItem(cartLists.get(position).getItemId());
            cartLists.remove(position);
            //create func
            if(!cartLists.isEmpty()){
                cartRv.setVisibility(View.VISIBLE);
                cartEmpty.setVisibility(View.GONE);
                startSearch.setVisibility(View.GONE);
                carttext.setVisibility(View.VISIBLE);

            }else{
                cartRv.setVisibility(View.GONE);
                cartEmpty.setVisibility(View.VISIBLE);
                startSearch.setVisibility(View.VISIBLE);
                carttext.setVisibility(View.GONE);
            }
        });
        cartRv.setAdapter(adapter);


        if(!cartLists.isEmpty()){
            cartRv.setVisibility(View.VISIBLE);
            cartEmpty.setVisibility(View.GONE);
            startSearch.setVisibility(View.GONE);
            carttext.setVisibility(View.VISIBLE);
        }else{
            cartRv.setVisibility(View.GONE);
            cartEmpty.setVisibility(View.VISIBLE);
            startSearch.setVisibility(View.VISIBLE);
            carttext.setVisibility(View.GONE);
        }

        return view;
    }
}