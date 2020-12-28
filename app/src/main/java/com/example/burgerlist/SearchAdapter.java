package com.example.burgerlist;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    private ArrayList<String> rest_id_list;
    private ArrayList<String> rest_name_list;
    private ArrayList<String> rating_list;


    class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView rest_name;
        TextView rating;
        TextView distance;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            logo = (ImageView)itemView.findViewById(R.id.rest_logo_image);
            rest_name = (TextView)itemView.findViewById(R.id.restname_item);
            rating = (TextView)itemView.findViewById(R.id.rating_item);
            distance = (TextView)itemView.findViewById(R.id.distance_item);

        }
    }

    public SearchAdapter(Context context, ArrayList<String> rest_id_list, ArrayList<String> rest_name_list, ArrayList<String> rating_list) {
        this.context = context;
        this.rest_id_list = rest_id_list;
        this.rest_name_list = rest_name_list;
        this.rating_list = rating_list;
    }


    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_result_item,parent,false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.rest_name.setText(rest_name_list.get(position));
        holder.rating.setText(rating_list.get(position));

        // need picture for logo and distance here.
    }



    @Override
    public int getItemCount() {
        return rest_name_list.size();
    }
}
