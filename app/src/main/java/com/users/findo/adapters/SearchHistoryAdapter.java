package com.users.findo.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.users.findo.databaseClass.SearchHistoryDb;
import com.users.findo.R;

import java.util.ArrayList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<String> searchHistory;
    Context context;


    public SearchHistoryAdapter(Context context, ArrayList<String> searchHistory){
        this.context = context;
        this.searchHistory = searchHistory;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_tabs_items,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {



        SearchViewHolder myViewHolder = (SearchViewHolder) holder;
        myViewHolder.textView.setText(searchHistory.get(position));


        myViewHolder.itemView.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Alert");
            builder.setMessage("Do you want to delete item?");
            builder.setCancelable(true);

            builder.setPositiveButton("Yes", (dialog, which) -> {
                // Delete the item here
                SearchHistoryDb searchHistoryDb = new SearchHistoryDb(context);
                searchHistoryDb.deleteItem(searchHistory.get(holder.getAdapterPosition()));
                searchHistory.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            });

            builder.setNegativeButton("No", (dialog, which) -> {
                // Dismiss the dialog box
                dialog.cancel();
            });

            AlertDialog alert = builder.create();
            alert.show();
            return false;
        });


    }

    @Override
    public int getItemCount() {
        return searchHistory.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.searchTabText);
        }

    }
}
